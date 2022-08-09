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
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Vote = Vote(id, memberId, postId, choiceId)

    fun changeChoice(choiceId: Long, memberId: Long) =
        Vote(id, memberId, postId, choiceId, createdDateTime, lastModifiedDateTime)
}
