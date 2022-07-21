package teamversus.naenio.api.domain.choice.application

import reactor.core.publisher.Mono

interface ChoiceDeleteUseCase {
    fun deleteAllByPostId(id: Long): Mono<Void>
}