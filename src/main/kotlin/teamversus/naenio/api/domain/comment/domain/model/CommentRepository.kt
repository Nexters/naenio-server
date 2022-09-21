package teamversus.naenio.api.domain.comment.domain.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface CommentRepository : ReactiveCrudRepository<Comment, Long> {
    fun existsByIdAndParentType(id: Long, parentType: CommentParent): Mono<Boolean>
    fun existsByIdAndMemberId(id: Long, memberId: Long): Mono<Boolean>

    fun countByParentIdAndParentTypeAndMemberIdNotIn(
        parentId: Long,
        parentType: CommentParent,
        excludeMemberIds: List<Long>,
    ): Mono<Long>

    fun findAllByIdLessThanAndParentIdAndParentTypeOrderByIdDesc(
        id: Long,
        parentId: Long,
        parentType: CommentParent,
        pageable: Pageable,
    ): Flux<Comment>

    fun findAllByIdLessThanAndMemberIdNotInAndParentIdAndParentTypeOrderByIdDesc(
        id: Long,
        excludeMemberIds: List<Long>,
        parentId: Long,
        parentType: CommentParent,
        pageable: Pageable,
    ): Flux<Comment>

    fun findAllByIdLessThanAndMemberIdAndParentTypeOrderByIdDesc(
        id: Long,
        memberId: Long,
        parentType: CommentParent,
        pageable: Pageable,
    ): Flux<Comment>

    fun findByIdAndMemberId(id: Long, memberId: Long): Mono<Comment>
}