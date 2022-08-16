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
            .switchIfEmpty { Mono.error(IllegalArgumentException("삭제된 댓글입니다.")) }
            .flatMap { commentLikeRepository.save(command.toDomain(memberId)) }
            .map { CommentLikeCreateUseCase.Result.of(it) }

    @Transactional
    override fun unlike(id: Long, memberId: Long): Mono<Void> =
        Mono.just(id)
            .filterWhen { commentLikeRepository.existsByMemberIdAndCommentId(memberId, it) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("일시적으로 연결 상태가 불안정하여 요청을 처리할 수 없습니다. 관리자에게 문의 해주세요.")) }
            .flatMap { commentLikeRepository.deleteByMemberIdAndCommentId(memberId, id) }
}