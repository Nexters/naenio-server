package teamversus.naenio.api.domain.category.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.category.application.CategoryExistUseCase
import teamversus.naenio.api.domain.category.application.CategoryLoadUseCase
import teamversus.naenio.api.domain.category.domain.model.Category
import teamversus.naenio.api.domain.category.domain.model.CategoryRepository

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
) : CategoryExistUseCase, CategoryLoadUseCase {
    override fun existById(id: Long): Mono<Boolean> =
        categoryRepository.existsById(id)

    override fun findById(id: Long): Mono<Category> =
        categoryRepository.findById(id)
}