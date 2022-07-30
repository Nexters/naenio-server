package teamversus.naenio.api.domain.commentlike.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class CommentLike(
    @Id
    val id: Long = 0,
    val commentId: Long,
    val memberId: Long,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): CommentLike = CommentLike(id, commentId, memberId)
}