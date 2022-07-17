package teamversus.naenio.api.command.post.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PostRepository : ReactiveCrudRepository<Post, Long> {
}