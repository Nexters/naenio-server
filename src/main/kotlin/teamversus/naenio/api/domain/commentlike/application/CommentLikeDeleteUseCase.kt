package teamversus.naenio.api.domain.commentlike.application

import reactor.core.publisher.Mono

interface CommentLikeDeleteUseCase {
    fun unlike(id: Long, memberId: Long): Mono<Void>
}