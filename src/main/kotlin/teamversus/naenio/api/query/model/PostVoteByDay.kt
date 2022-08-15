package teamversus.naenio.api.query.model

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class PostVoteByDay(
    @Id
    val id: Long = 0,
    val postId: Long,
    val aggregateDate: LocalDate,
    val count: Long,
) {
    fun withId(id: Long) = PostVoteByDay(id, postId, aggregateDate, count)

    fun increaseCount() = PostVoteByDay(id, postId, aggregateDate, count + 1L)
}