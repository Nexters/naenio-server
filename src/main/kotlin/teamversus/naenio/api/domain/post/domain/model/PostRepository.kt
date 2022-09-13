package teamversus.naenio.api.domain.post.domain.model

import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostRepository : ReactiveCrudRepository<Post, Long> {
    fun findAllByIdLessThanOrderByIdDesc(id: Long, pageable: Pageable): Flux<Post>
    fun findAllByMemberIdAndIdLessThanOrderByIdDesc(memberId: Long, id: Long, pageable: Pageable): Flux<Post>
    fun existsByIdAndMemberId(id: Long, memberId: Long): Mono<Boolean>

    @Query(value = "SELECT * FROM post ORDER BY RAND() LIMIT 1")
    fun findByRandom(): Mono<Post>
}