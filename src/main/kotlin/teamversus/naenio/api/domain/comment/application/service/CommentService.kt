package teamversus.naenio.api.domain.comment.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.comment.application.CommentCreateUseCase
import teamversus.naenio.api.domain.comment.application.CommentDeleteUseCase
import teamversus.naenio.api.domain.comment.domain.event.CommentCreatedEvent
import teamversus.naenio.api.domain.comment.domain.event.CommentDeletedEvent
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import teamversus.naenio.api.domain.comment.domain.model.CommentRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.query.handler.CommentQueryEventHandler

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val commentQueryEventHandler: CommentQueryEventHandler,
) : CommentCreateUseCase, CommentDeleteUseCase {
    override fun create(command: CommentCreateUseCase.Command, memberId: Long): Mono<CommentCreateUseCase.Result> {
        require(command.content.length <= 200) { "댓글은 최대 200자 입니다." }

        return Mono.just(command)
            .filterWhen(::isParentCommentable)
            .switchIfEmpty { Mono.error(IllegalArgumentException("이미 삭제된 글입니다.")) }
            .flatMap {
                commentRepository.save(command.toDomain(memberId))
                    .doOnSuccess {
                        commentQueryEventHandler.handle(
                            CommentCreatedEvent(
                                it.id,
                                it.memberId,
                                it.parentId,
                                it.parentType,
                                it.content,
                                it.createdDateTime,
                                it.lastModifiedDateTime
                            )
                        )
                    }
            }
            .map { CommentCreateUseCase.Result.of(it) }
    }


    private fun isParentCommentable(it: CommentCreateUseCase.Command): Mono<Boolean> {
        if (it.parentType == CommentParent.POST) {
            return postRepository.existsById(it.parentId)
        }
        if (it.parentType == CommentParent.COMMENT) {
            return commentRepository.existsByIdAndParentType(it.parentId, CommentParent.POST)
        }
        return Mono.just(false)
    }


    @Transactional
    override fun delete(id: Long, memberId: Long): Mono<Void> =
        commentRepository.findByIdAndMemberId(id, memberId)
            .switchIfEmpty { Mono.error(IllegalArgumentException("이미 삭제된 댓글입니다.")) }
            .flatMap { comment ->
                commentRepository.deleteById(id)
                    .doOnSuccess {
                        commentQueryEventHandler.handle(
                            CommentDeletedEvent(
                                comment.id,
                                comment.memberId,
                                comment.parentId,
                                comment.parentType,
                                comment.content,
                                comment.createdDateTime,
                                comment.lastModifiedDateTime
                            )
                        )
                    }
            }
}