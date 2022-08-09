package teamversus.naenio.api.domain.vote.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.vote.application.VoteCastUseCase
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository

@Service
class VoteService(private val voteRepository: VoteRepository) : VoteCastUseCase {
    override fun cast(command: VoteCastUseCase.Command, memberId: Long): Mono<VoteCastUseCase.Result> =
        voteRepository.findByPostIdAndMemberId(command.postId, memberId)
            .map { it.changeChoice(command.choiceId, memberId) }
            .switchIfEmpty(Mono.just(command.toDomain(memberId)))
            .flatMap { voteRepository.save(it) }
            .map { VoteCastUseCase.Result.of(it) }
}
