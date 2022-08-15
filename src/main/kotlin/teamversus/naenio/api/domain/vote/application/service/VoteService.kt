package teamversus.naenio.api.domain.vote.application.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.vote.application.VoteCastUseCase
import teamversus.naenio.api.domain.vote.domain.event.VoteChangedEvent
import teamversus.naenio.api.domain.vote.domain.event.VoteCreatedEvent
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository

@Service
class VoteService(
    private val voteRepository: VoteRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : VoteCastUseCase {
    override fun cast(command: VoteCastUseCase.Command, memberId: Long): Mono<VoteCastUseCase.Result> =
        voteRepository.findByPostIdAndMemberId(command.postId, memberId)
            .map { it.changeChoice(command.choiceId, memberId) }
            .flatMap {
                voteRepository.save(it)
                    .doOnSuccess { vote ->
                        VoteChangedEvent(
                            vote.id,
                            vote.memberId,
                            vote.postId,
                            vote.choiceId,
                            vote.lastModifiedDateTime,
                        )
                    }
            }
            .switchIfEmpty(Mono.just(command.toDomain(memberId)))
            .flatMap {
                voteRepository.save(it)
                    .doOnSuccess { vote ->
                        applicationEventPublisher.publishEvent(
                            VoteCreatedEvent(
                                vote.id,
                                vote.memberId,
                                vote.postId,
                                vote.choiceId,
                                vote.createdDateTime
                            )
                        )
                    }
            }
            .map { VoteCastUseCase.Result.of(it) }
}
