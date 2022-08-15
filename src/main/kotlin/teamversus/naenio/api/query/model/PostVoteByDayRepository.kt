package teamversus.naenio.api.query.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

interface PostVoteByDayRepository : ReactiveCrudRepository<PostVoteByDay, Long> {
    fun findByPostIdAndAggregateDate(postId: Long, aggregateDate: LocalDate): Mono<PostVoteByDay>
    fun findAllByAggregateDateOrderByCountDesc(aggregateDate: LocalDate, pageable: Pageable): Flux<PostVoteByDay>
}