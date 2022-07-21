package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.category.domain.model.CategoryRepository
import teamversus.naenio.api.query.result.AppCategoriesQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class AppCategoryFetcher(
    private val categoryRepository: CategoryRepository,
) {
    fun findAll(request: ServerRequest): Mono<ServerResponse> =
        categoryRepository.findAll()
            .collectList()
            .map {
                AppCategoriesQueryResult(it.map { category ->
                    AppCategoriesQueryResult.Category(
                        category.id,
                        category.name
                    )
                })
            }
            .flatMap(::okWithBody)
}
