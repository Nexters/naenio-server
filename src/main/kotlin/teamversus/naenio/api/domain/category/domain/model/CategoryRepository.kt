package teamversus.naenio.api.domain.category.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CategoryRepository : ReactiveCrudRepository<Category, Long> {

}
