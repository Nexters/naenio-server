package teamversus.naenio.api.domain.category.application

import reactor.core.publisher.Mono

interface CategoryExistUseCase {
    fun existById(id: Long): Mono<Boolean>
}
