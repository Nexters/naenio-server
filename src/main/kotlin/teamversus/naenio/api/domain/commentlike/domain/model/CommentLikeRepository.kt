package teamversus.naenio.api.domain.commentlike.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CommentLikeRepository : ReactiveCrudRepository<CommentLike, Long> {
    fun countByCommentId(commentId: Long): Mono<Long>

    fun existsByMemberIdAndCommentId(memberId: Long, commentId: Long): Mono<Boolean>
}