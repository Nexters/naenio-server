package teamversus.naenio.api.query.result

data class AppNoticesQueryResult(
    val notices: List<NoticeResult>,
) {
    data class NoticeResult(
        val id: Long,
        val title: String,
        val content: String,
    )
}