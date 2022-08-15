package teamversus.naenio.api.query.model

import org.springframework.data.annotation.Id

data class PostVoteCount(
    @Id
    val id: Long = 0,
    val postId: Long,
    val count: Long,
) {
    fun withId(id: Long) = PostVoteCount(id, postId, count)

    fun increaseCount() = PostVoteCount(id, postId, count + 1L)
}