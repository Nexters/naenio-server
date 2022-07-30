package teamversus.naenio.api.domain.commentlike.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.comment.domain.model.CommentRepository
import teamversus.naenio.api.domain.commentlike.application.CommentLikeCreateUseCase
import teamversus.naenio.api.domain.commentlike.application.CommentLikeDeleteUseCase
import teamversus.naenio.api.domain.commentlike.domain.model.CommentLikeRepository

@Service
class CommentLikeService(
    private val commentLikeRepository: CommentLikeRepository,
    private val commentRepository: CommentRepository,
) : CommentLikeCreateUseCase, CommentLikeDeleteUseCase {
    override fun like(
        command: CommentLikeCreateUseCase.Command,
        memberId: Long,
    ): Mono<CommentLikeCreateUseCase.Result> =
        Mono.just(command)
            .filterWhen { commentRepository.existsById(it.commentId) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("댓글이 존재하지 않습니다. commentId=${command.commentId}")) }
            .flatMap { commentLikeRepository.save(command.toDomain(memberId)) }
            .map { CommentLikeCreateUseCase.Result.of(it) }

    @Transactional
    override fun unlike(id: Long, memberId: Long): Mono<Void> =
        commentLikeRepository.existsById(id)
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 댓글 입니다. id=${id}}")) }
            .flatMap { commentLikeRepository.deleteById(id) }
}