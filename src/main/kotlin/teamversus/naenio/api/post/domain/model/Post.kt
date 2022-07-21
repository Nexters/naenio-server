package teamversus.naenio.api.post.domain.model

import org.springframework.data.annotation.Id

class Post(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val title: String,
    val content: String,
) {
    fun withId(id: Long): Post = Post(id, memberId, title, content)
}

