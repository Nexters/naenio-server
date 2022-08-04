package teamversus.naenio.api.query.result


data class AppFeedQueryResult(
    val posts: List<Post>,
) {

    data class Post(
        val id: Long,
        val author: Author,
        val title: String,
        val content: String,
        val choices: List<Choice>,
        val commentCount: Long,
    ) {
        data class Author(
            val id: Long,
            val nickname: String?,
            val profileImageIndex: Int?,
        )

        data class Choice(
            val id: Long,
            val sequence: Int,
            val name: String,
            val isSelected: Boolean,
            val voteCount: Long,
        )
    }
}