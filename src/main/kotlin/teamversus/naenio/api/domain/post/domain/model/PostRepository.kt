package teamversus.naenio.api.domain.post.domain.model

import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostRepository : ReactiveCrudRepository<Post, Long> {
    fun findAllByIdLessThanAndMemberIdNotInOrderByIdDesc(
        id: Long,
        excludeMemberIds: List<Long>,
        pageable: Pageable,
    ): Flux<Post>

    fun findAllByMemberIdAndIdLessThanOrderByIdDesc(memberId: Long, id: Long, pageable: Pageable): Flux<Post>
    fun existsByIdAndMemberId(id: Long, memberId: Long): Mono<Boolean>

    @Query(value = "SELECT * FROM post WHERE post.member_id NOT IN :memberIds")
    fun findByRandomAndMemberIdNotIn(memberIds: List<Long>): Mono<Post>

    fun findByIdAndMemberIdNotIn(id: Long, memberId: List<Long>): Mono<Post>
}