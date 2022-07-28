package teamversus.naenio.api.domain.comment.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Comment(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val parentId: Long,
    val parentType: CommentParent,
    val content: String,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Comment = Comment(id, memberId, parentId, parentType, content)
}
