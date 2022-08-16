package teamversus.naenio.api.domain.comment.web

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.comment.application.CommentCreateUseCase
import teamversus.naenio.api.domain.comment.application.CommentDeleteUseCase
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
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
        commentDeleteUseCase.delete(request.pathVariable("id").toLong(), request.memberId())
            .flatMap { ServerResponse.noContent().build() }


    data class CreateCommentRequest(
        val parentId: Long,
        val parentType: CommentParent,
        val content: String,
    ) {
        fun toCommand(): CommentCreateUseCase.Command = CommentCreateUseCase.Command(parentId, parentType, content)
    }

    data class CreateCommentResponse(
        val id: Long,
        val memberId: Long,
        val parentId: Long,
        val parentType: CommentParent,
        val content: String,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        val createdDateTime: LocalDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun from(result: CommentCreateUseCase.Result) =
                CreateCommentResponse(
                    result.id,
                    result.memberId,
                    result.parentId,
                    result.parentType,
                    result.content,
                    result.createdDateTime,
                    result.lastModifiedDateTime
                )
        }
    }
}




