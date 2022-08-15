package teamversus.naenio.api.query.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostChoiceVoteCountRepository : ReactiveCrudRepository<PostChoiceVoteCount, Long> {
    fun findByPostId(postId: Long): Mono<PostChoiceVoteCount>
    fun findAllByTotalCountGreaterThanEqualOrderByRateDesc(
        totalCount: Int,
        pageable: Pageable,
    ): Flux<PostChoiceVoteCount>

    fun findAllByTotalCountGreaterThanEqualOrderByRateAsc(
        totalCount: Int,
        pageable: Pageable,
    ): Flux<PostChoiceVoteCount>
}