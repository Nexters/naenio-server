package teamversus.naenio.api.query.result

data class AppPostCommentsOfMeQueryResult(
    val comments: List<AppPostCommentsOfMe>,
) {

    data class AppPostCommentsOfMe(
        val id: Long,
        val content: String,
        val post: AppPostCommentsOfMePost,
    ) {
        data class AppPostCommentsOfMePost(
            val id: Long,
            val author: AppPostCommentsOfMePostAuthor,
            val title: String,
        ) {
            data class AppPostCommentsOfMePostAuthor(
                val id: Long,
                val nickname: String?,
                val profileImageIndex: Int?,
            )
        }
    }

}
