package teamversus.naenio.api.domain.comment.domain.event

import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import java.time.LocalDateTime

data class CommentDeletedEvent(
    val id: Long,
    val memberId: Long,
    val parentId: Long,
    val parentType: CommentParent,
    val content: String,
    val createdDateTime: LocalDateTime,
    val lastModifiedDateTime: LocalDateTime,
) {
}