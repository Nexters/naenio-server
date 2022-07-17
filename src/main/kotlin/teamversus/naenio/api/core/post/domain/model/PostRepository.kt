package teamversus.naenio.api.core.post.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PostRepository : ReactiveCrudRepository<Post, Long> {
}