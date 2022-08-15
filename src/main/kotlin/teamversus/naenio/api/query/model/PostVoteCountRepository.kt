package teamversus.naenio.api.query.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostVoteCountRepository : ReactiveCrudRepository<PostVoteCount, Long> {
    fun findByPostId(postId: Long): Mono<PostVoteCount>

    fun findAllByOrderByCountDesc(pageable: Pageable): Flux<PostVoteCount>
}