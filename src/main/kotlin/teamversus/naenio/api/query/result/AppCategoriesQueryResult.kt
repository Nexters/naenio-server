package teamversus.naenio.api.query.result

data class AppCategoriesQueryResult(
    val categories: List<Category>,
) {
    data class Category(
        val id: Long,
        val name: String,
    )
}
