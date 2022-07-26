package teamversus.naenio.api.domain.comment.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.comment.model.Comment
import java.time.LocalDateTime

interface CommentCreateUseCase {
    fun create(command: Command, memberId: Long): Mono<Result>

    data class Command(
        val postId: Long,
        val content: String,
    ) {
        fun toDomain(memberId: Long): Comment =
            Comment(0, memberId, content)
    }

    data class Result(
        val id: Long,
        val memberId: Long,
        val content: String,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun of(comment: Comment): Result =
                Result(
                    comment.id,
                    comment.memberId,
                    comment.content,
                    comment.createdDateTime,
                    comment.lastModifiedDateTime
                )
        }
    }
}
