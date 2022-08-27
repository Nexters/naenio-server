package teamversus.naenio.api.query.model

import org.springframework.data.annotation.Id

data class Notice(
    @Id
    val id: Long = 0,
    val title: String,
    val content: String,
) {
    fun withId(id: Long): Notice = Notice(id, title, content)
}
