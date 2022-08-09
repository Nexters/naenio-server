package teamversus.naenio.api.query.result

import java.time.LocalDateTime

data class AppPostCommentsQueryResult(
    val totalCommentCount: Long,
    val comments: List<Comment>,
) {
    data class Comment(
        val id: Long,
        val author: Author,
        val content: String,
        val createdDatetime: LocalDateTime,
        val likeCount: Long,
        val isLiked: Boolean,
        val repliesCount: Long,
    ) {
        data class Author(
            val id: Long,
            val nickname: String?,
            val profileImageIndex: Int?,
        )
    }
}