package teamversus.naenio.api.domain.comment.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.comment.domain.model.Comment
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import java.time.LocalDateTime

interface CommentCreateUseCase {
    fun create(command: Command, memberId: Long): Mono<Result>

    data class Command(
        val parentId: Long,
        val parentType: CommentParent,
        val content: String,
    ) {
        fun toDomain(memberId: Long): Comment =
            Comment(0, memberId, parentId, parentType, content)
    }

    data class Result(
        val id: Long,
        val memberId: Long,
        val parentId: Long,
        val parentType: CommentParent,
        val content: String,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun of(comment: Comment): Result =
                Result(
                    comment.id,
                    comment.memberId,
                    comment.parentId,
                    comment.parentType,
                    comment.content,
                    comment.createdDateTime,
                    comment.lastModifiedDateTime
                )
        }
    }
}
