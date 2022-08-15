package teamversus.naenio.api.query.result

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AppPostCommentsQueryResult(
    val totalCommentCount: Long,
    val comments: List<AppPostComment>,
) {
    data class AppPostComment(
        val id: Long,
        val author: AppPostCommentAuthor,
        val content: String,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        val createdDatetime: LocalDateTime,
        val likeCount: Long,
        val isLiked: Boolean,
        val repliesCount: Long,
    ) {
        data class AppPostCommentAuthor(
            val id: Long,
            val nickname: String?,
            val profileImageIndex: Int?,
        )
    }
}