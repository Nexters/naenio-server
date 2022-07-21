package teamversus.naenio.api.post.application

import reactor.core.publisher.Mono

interface PostDeleteUseCase {
    fun deleteById(id: Long): Mono<Void>
}
