package teamversus.naenio.api.core.choice.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ChoiceRepository : ReactiveCrudRepository<Choice, Long> {
    fun findAllByPostId(postId: Long): Flux<Choice>
}