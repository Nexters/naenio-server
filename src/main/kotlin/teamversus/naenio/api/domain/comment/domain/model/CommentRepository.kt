package teamversus.naenio.api.domain.comment.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CommentRepository : ReactiveCrudRepository<Comment, Long> {
    fun existsByIdAndParentType(id: Long, parentType: CommentParent): Mono<Boolean>

    fun countByParentIdAndParentType(parentId: Long, parentType: CommentParent): Mono<Long>
}