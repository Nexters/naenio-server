package teamversus.naenio.api.domain.comment.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.comment.application.CommentCreateUseCase
import teamversus.naenio.api.domain.comment.application.CommentDeleteUseCase
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.support.okWithBody
import java.time.LocalDateTime

@Component
class CommentHandler(
    private val commentCreateUseCase: CommentCreateUseCase,
    private val commentDeleteUseCase: CommentDeleteUseCase,
) {
    fun create(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(CreateCommentRequest::class.java)
            .flatMap { commentCreateUseCase.create(it.toCommand(), request.memberId()) }
            .map { CreateCommentResponse.from(it) }
            .flatMap(::okWithBody)

    fun delete(request: ServerRequest): Mono<ServerResponse> =
        commentDeleteUseCase.delete(request.pathVariable("id").toLong())
            .flatMap { ServerResponse.noContent().build() }


    data class CreateCommentRequest(
        val postId: Long,
        val content: String,
    ) {
        fun toCommand(): CommentCreateUseCase.Command = CommentCreateUseCase.Command(postId, content)
    }

    data class CreateCommentResponse(
        val id: Long,
        val memberId: Long,
        val content: String,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun from(result: CommentCreateUseCase.Result) =
                CreateCommentResponse(
                    result.id,
                    result.memberId,
                    result.content,
                    result.createdDateTime,
                    result.lastModifiedDateTime
                )
        }
    }
}




