package teamversus.naenio.api.domain.category.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.category.domain.model.Category

interface CategoryLoadUseCase {
    fun findById(id: Long): Mono<Category>
}
