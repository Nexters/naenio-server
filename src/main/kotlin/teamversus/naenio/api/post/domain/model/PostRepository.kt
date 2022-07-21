package teamversus.naenio.api.post.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PostRepository : ReactiveCrudRepository<Post, Long> {
}