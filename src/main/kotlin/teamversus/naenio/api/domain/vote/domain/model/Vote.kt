package teamversus.naenio.api.domain.vote.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Vote(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val postId: Long,
    val choiceId: Long,
    val postAuthorId: Long,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Vote = Vote(id, memberId, postId, choiceId, postAuthorId)

    fun changeChoice(choiceId: Long, memberId: Long): Vote {
        require(this.choiceId != choiceId) { "같은 선택지에 투표할 수 없습니다." }
        return Vote(id, memberId, postId, choiceId, postAuthorId, createdDateTime, lastModifiedDateTime)
    }
}
