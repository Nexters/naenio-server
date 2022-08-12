package teamversus.naenio.api.domain.post.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Post(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val title: String,
    val content: String,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Post = Post(id, memberId, title, content)
}

