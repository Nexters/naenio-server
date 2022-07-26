package teamversus.naenio.api.domain.comment.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.comment.application.CommentCreateUseCase
import teamversus.naenio.api.domain.comment.model.CommentRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) : CommentCreateUseCase {
    override fun create(command: CommentCreateUseCase.Command, memberId: Long): Mono<CommentCreateUseCase.Result> =
        Mono.just(command)
            .filterWhen { postRepository.existsById(it.postId) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("게시글이 존재하지 않습니다. postId=${command.postId}")) }
            .flatMap { commentRepository.save(command.toDomain(memberId)) }
            .map { CommentCreateUseCase.Result.of(it) }

}