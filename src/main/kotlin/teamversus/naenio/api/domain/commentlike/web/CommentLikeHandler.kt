package teamversus.naenio.api.domain.commentlike.web

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.commentlike.application.CommentLikeCreateUseCase
import teamversus.naenio.api.domain.commentlike.application.CommentLikeDeleteUseCase
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.support.okWithBody
import java.time.LocalDateTime

@Component
class CommentLikeHandler(
    private val commentLikeCreateUseCase: CommentLikeCreateUseCase,
    private val commentLikeDeleteUseCase: CommentLikeDeleteUseCase,
) {
    fun like(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(CommentRequest::class.java)
            .flatMap { commentLikeCreateUseCase.like(it.toCommand(), request.memberId()) }
            .map { LikeCommentResponse.from(it) }
            .flatMap(::okWithBody)

    fun unlike(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(CommentRequest::class.java)
            .flatMap { commentLikeDeleteUseCase.unlike(it.commentId, request.memberId()) }
            .flatMap { ServerResponse.noContent().build() }

    data class CommentRequest(
        val commentId: Long,
    ) {
        fun toCommand(): CommentLikeCreateUseCase.Command = CommentLikeCreateUseCase.Command(commentId)
    }

    data class LikeCommentResponse(
        val id: Long,
        val commentId: Long,
        val memberId: Long,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        val createdDateTime: LocalDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun from(result: CommentLikeCreateUseCase.Result) =
                LikeCommentResponse(
                    result.id,
                    result.commentId,
                    result.memberId,
                    result.createdDateTime,
                    result.lastModifiedDateTime
                )
        }
    }
}




