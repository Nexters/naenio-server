package teamversus.naenio.api.query.result

data class AppPostDetailQueryResult(
    val id: Long,
    val author: AppPostDetailAuthor,
    val title: String,
    val content: String,
    val choices: List<AppPostDetailChoice>,
    val commentCount: Long,
) {
    data class AppPostDetailAuthor(
        val id: Long,
        val nickname: String?,
        val profileImageIndex: Int?,
    )

    data class AppPostDetailChoice(
        val id: Long,
        val sequence: Int,
        val name: String,
        val isVoted: Boolean,
        val voteCount: Long,
    )
}