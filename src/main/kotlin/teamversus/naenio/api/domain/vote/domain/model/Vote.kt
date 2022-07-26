package teamversus.naenio.api.domain.vote.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Vote(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val choiceId: Long,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Vote = Vote(id, memberId, choiceId)

    fun changeChoice(choiceId: Long, memberId: Long) =
        if (this.memberId == memberId) {
            Vote(id, memberId, choiceId, createdDateTime, lastModifiedDateTime)
        } else {
            throw IllegalArgumentException("memberId가 일치하지 않습니다. vote.memberId=${this.memberId}, request.memberId=${memberId}")
        }
}
