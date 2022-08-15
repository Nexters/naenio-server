package teamversus.naenio.api.query.result

data class WebPostDetailQueryResult(
    val id: Long,
    val author: WebPostDetailAuthor,
    val title: String,
    val content: String,
    val choices: List<WebPostDetailChoice>,
    val commentCount: Long,
    val totalVoteCount: Long,
) {
    data class WebPostDetailAuthor(
        val id: Long,
        val nickname: String?,
        val profileImageIndex: Int?,
    )

    data class WebPostDetailChoice(
        val id: Long,
        val sequence: Int,
        val name: String,
    )
}