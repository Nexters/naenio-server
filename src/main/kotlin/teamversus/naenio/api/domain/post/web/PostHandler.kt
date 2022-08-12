package teamversus.naenio.api.domain.post.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.post.application.PostCreateUseCase
import teamversus.naenio.api.domain.post.application.PostDeleteUseCase
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.support.okWithBody
import java.time.LocalDateTime

@Component
class PostHandler(
    private val postCreateUseCase: PostCreateUseCase,
    private val postDeleteUseCase: PostDeleteUseCase,
) {
    fun create(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(CreatePostRequest::class.java)
            .flatMap { postCreateUseCase.create(it.toCommand(), request.memberId()) }
            .map { CreatePostResponse.from(it) }
            .flatMap(::okWithBody)

    fun delete(request: ServerRequest): Mono<ServerResponse> =
        postDeleteUseCase.deleteById(request.pathVariable("id").toLong())
            .flatMap { ServerResponse.noContent().build() }

    data class CreatePostRequest(
        val title: String,
        val content: String,
        val categoryId: Long,
        val choices: List<CreateChoiceRequest>,
    ) {
        data class CreateChoiceRequest(
            val name: String,
        )

        fun toCommand(): PostCreateUseCase.Command =
            PostCreateUseCase.Command(
                title,
                content,
                choices.map { PostCreateUseCase.Command.ChoiceCommand(it.name) })
    }

    data class CreatePostResponse(
        val id: Long,
        val memberId: Long,
        val title: String,
        val content: String,
        val choices: List<ChoiceResponse>,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun from(result: PostCreateUseCase.Result): CreatePostResponse =
                CreatePostResponse(
                    result.id,
                    result.memberId,
                    result.title,
                    result.content,
                    result.choices.map { ChoiceResponse(it.id, it.sequence, it.name) },
                    result.createdDateTime,
                    result.lastModifiedDateTime,
                )
        }

        data class ChoiceResponse(
            val id: Long,
            val sequence: Int,
            val name: String,
        )
    }
}




