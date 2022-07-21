package teamversus.naenio.api.domain.choice.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ChoiceRepository : ReactiveCrudRepository<Choice, Long> {
    fun findAllByPostId(postId: Long): Flux<Choice>
    fun deleteAllByPostId(id: Long): Mono<Void>
}