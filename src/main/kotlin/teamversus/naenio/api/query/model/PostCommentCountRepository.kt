package teamversus.naenio.api.query.model

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostCommentCountRepository : ReactiveCrudRepository<PostCommentCount, Long> {
    fun findByPostId(postId: Long): Mono<PostCommentCount>
    fun findAllByOrderByCommentCountDesc(pageable: Pageable): Flux<PostCommentCount>
}