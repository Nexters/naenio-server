package teamversus.naenio.api.query.result

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AppCommentRepliesQueryResult(
    val commentReplies: List<CommentReply>,
) {
    data class CommentReply(
        val id: Long,
        val author: Author,
        val content: String,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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