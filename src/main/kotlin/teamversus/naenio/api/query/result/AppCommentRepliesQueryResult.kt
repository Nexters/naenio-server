package teamversus.naenio.api.query.result

import java.time.LocalDateTime

data class AppCommentRepliesQueryResult(
    val commentReplies: List<CommentReply>,
) {
    data class CommentReply(
        val id: Long,
        val author: Author,
        val createdDatetime: LocalDateTime,
        val likeCount: Long,
        val isLiked: Boolean,
    ) {
        data class Author(
            val id: Long,
            val nickname: String?,
            val profileImageIndex: Int?,
        )
    }
}