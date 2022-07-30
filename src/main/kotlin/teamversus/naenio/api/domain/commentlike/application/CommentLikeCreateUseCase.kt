package teamversus.naenio.api.domain.commentlike.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.commentlike.domain.model.CommentLike
import java.time.LocalDateTime

interface CommentLikeCreateUseCase {
    fun like(command: Command, memberId: Long): Mono<Result>

    data class Command(
        val commentId: Long,
    ) {
        fun toDomain(memberId: Long) = CommentLike(0, commentId, memberId)
    }

    data class Result(
        val id: Long,
        val memberId: Long,
        val commentId: Long,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun of(commentLike: CommentLike) = Result(
                commentLike.id,
                commentLike.memberId,
                commentLike.commentId,
                commentLike.createdDateTime,
                commentLike.lastModifiedDateTime,
            )
        }
    }
}