package teamversus.naenio.api.query.model

import org.springframework.data.annotation.Id

data class PostCommentCount(
    @Id
    val id: Long = 0,
    val postId: Long,
    val commentCount: Int = 0,
) {
    fun withId(id: Long) = PostCommentCount(id, postId, commentCount)

    fun increaseCommentCount() = PostCommentCount(id, postId, commentCount + 1)
}