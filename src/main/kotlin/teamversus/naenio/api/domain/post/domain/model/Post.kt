package teamversus.naenio.api.domain.post.domain.model

import org.springframework.data.annotation.Id
import teamversus.naenio.api.domain.support.AggregateRoot

data class Post(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val title: String,
    val content: String,
    val categoryId: Long,
) : AggregateRoot<Post>() {
    fun withId(id: Long): Post = Post(id, memberId, title, content, categoryId)
}

