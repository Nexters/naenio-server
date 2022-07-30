package teamversus.naenio.api.domain.commentlike.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CommentLikeRepository : ReactiveCrudRepository<CommentLike, Long> {
}