package teamversus.naenio.api.domain.vote.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.vote.domain.model.Vote
import java.time.LocalDateTime

interface VoteCastUseCase {
    fun cast(command: Command, memberId: Long): Mono<Result>

    data class Command(
        val postId: Long,
        val choiceId: Long,
        val previousVoteId: Long?,
    ) {
        fun toDomain(memberId: Long): Vote =
            Vote(0, memberId, choiceId)
    }

    data class Result(
        val id: Long,
        val choiceId: Long,
        val memberId: Long,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun of(vote: Vote): Result =
                Result(vote.id, vote.choiceId, vote.memberId, vote.createdDateTime, vote.lastModifiedDateTime)
        }
    }
}
