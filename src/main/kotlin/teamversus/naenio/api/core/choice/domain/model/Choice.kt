package teamversus.naenio.api.core.choice.domain.model

import org.springframework.data.annotation.Id

data class Choice(
    @Id
    val id: Long = 0,
    val postId: Long,
    val sequence: Int,
    val name: String,
) {
    fun withId(id: Long) = Choice(id, postId, sequence, name)
}