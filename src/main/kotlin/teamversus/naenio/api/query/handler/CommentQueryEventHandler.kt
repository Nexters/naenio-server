package teamversus.naenio.api.query.handler

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.comment.domain.event.CommentCreatedEvent
import teamversus.naenio.api.domain.comment.domain.event.CommentDeletedEvent
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import teamversus.naenio.api.query.model.PostCommentCount
import teamversus.naenio.api.query.model.PostCommentCountRepository

@Component
class CommentQueryEventHandler(
    private val postCommentCountRepository: PostCommentCountRepository,
) {
    fun handle(event: CommentCreatedEvent) {
        if (event.parentType != CommentParent.POST) {
            return
        }

        postCommentCountRepository.findByPostId(event.parentId)
            .switchIfEmpty(Mono.just(PostCommentCount(0, event.parentId, 0)))
            .map { it.increaseCommentCount() }
            .flatMap { postCommentCountRepository.save(it) }
            .subscribe()
    }

    fun handle(event: CommentDeletedEvent) {
        if (event.parentType != CommentParent.POST) {
            return
        }

        postCommentCountRepository.findByPostId(event.parentId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("댓글 개수가 존재하지 않습니다.")))
            .map { it.decreaseCommentCount() }
            .flatMap { postCommentCountRepository.save(it) }
            .subscribe()
    }
}