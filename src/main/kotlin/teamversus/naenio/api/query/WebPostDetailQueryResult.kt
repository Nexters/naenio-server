package teamversus.naenio.api.query

data class WebPostDetailQueryResult(
    val id: Long,
    val memberId: Long,
    val title: String,
    val content: String,
    val choices: List<Choice>,
) {
    data class Choice(
        val id: Long,
        val sequence: Int,
        val name: String,
    )
}
