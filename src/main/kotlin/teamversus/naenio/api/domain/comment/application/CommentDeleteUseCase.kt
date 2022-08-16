package teamversus.naenio.api.domain.comment.application

import reactor.core.publisher.Mono

interface CommentDeleteUseCase {
    fun delete(id: Long, memberId: Long): Mono<Void>
}
