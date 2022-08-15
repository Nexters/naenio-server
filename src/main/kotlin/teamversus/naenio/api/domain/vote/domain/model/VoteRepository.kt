package teamversus.naenio.api.domain.vote.domain.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface VoteRepository : ReactiveCrudRepository<Vote, Long> {
    fun existsByChoiceIdAndMemberId(choiceId: Long, memberId: Long): Mono<Boolean>

    fun countByChoiceId(choiceId: Long): Mono<Long>
    fun countByChoiceIdIn(choiceIds: List<Long>): Mono<Long>

    fun findByPostIdAndMemberId(postId: Long, memberId: Long): Mono<Vote>

    fun findByMemberIdAndLastModifiedDateTimeBeforeOrderByLastModifiedDateTimeDesc(
        memberId: Long,
        lastModifiedDateTime: LocalDateTime,
        pageable: Pageable,
    ): Flux<Vote>

    fun findAllByLastModifiedDateTimeBetween(from: LocalDateTime, to: LocalDateTime): Flux<Vote>
}