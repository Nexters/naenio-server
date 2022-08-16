package teamversus.naenio.api.domain.post.domain.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostRepository : ReactiveCrudRepository<Post, Long> {
    fun findAllByIdLessThanOrderByIdDesc(id: Long, pageable: Pageable): Flux<Post>
    fun findAllByMemberIdAndIdLessThanOrderByIdDesc(memberId: Long, id: Long, pageable: Pageable): Flux<Post>
    fun existsByIdAndMemberId(id: Long, memberId: Long): Mono<Boolean>
}