package teamversus.naenio.api.domain.category.domain.model

import org.springframework.data.annotation.Id

data class Category(
    @Id
    val id: Long = 0,
    val name: String,
) {
    fun withId(id: Long) = Category(id, name)
}
