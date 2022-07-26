package teamversus.naenio.api.domain.comment.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CommentRepository : ReactiveCrudRepository<Comment, Long>