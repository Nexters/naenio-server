package teamversus.naenio.api.domain.post.application

import reactor.core.publisher.Mono

interface PostDeleteUseCase {
    fun deleteById(id: Long, memberId: Long): Mono<Void>
}
