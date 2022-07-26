package teamversus.naenio.api.domain.vote.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.vote.application.VoteCastUseCase
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository

@Service
class VoteService(private val voteRepository: VoteRepository) : VoteCastUseCase {
    override fun cast(command: VoteCastUseCase.Command, memberId: Long): Mono<VoteCastUseCase.Result> =
        command.previousVoteId?.let { previousVoteId ->
            voteRepository.findById(previousVoteId)
                .switchIfEmpty(Mono.error(IllegalArgumentException("투표가 존재하지 않습니다. previousVoteId=${previousVoteId}")))
                .map { it.changeChoice(command.choiceId, memberId) }
                .flatMap { voteRepository.save(it) }
                .map { VoteCastUseCase.Result.of(it) }
        } ?: voteRepository.save(command.toDomain(memberId))
            .map { VoteCastUseCase.Result.of(it) }
}