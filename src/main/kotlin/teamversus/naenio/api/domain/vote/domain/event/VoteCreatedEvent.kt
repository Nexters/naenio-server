package teamversus.naenio.api.domain.vote.domain.event

import java.time.LocalDateTime

data class VoteCreatedEvent(
    val id: Long,
    val memberId: Long,
    val postId: Long,
    val choiceId: Long,
    val createdDateTime: LocalDateTime,
) {
}