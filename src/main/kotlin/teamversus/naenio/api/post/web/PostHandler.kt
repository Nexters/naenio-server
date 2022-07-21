package teamversus.naenio.api.post.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.post.application.PostCreateUseCase
import teamversus.naenio.api.post.application.PostEditUseCase
import teamversus.naenio.api.support.okWithBody

@Component
class PostHandler(
    private val postCreateUseCase: PostCreateUseCase,
    private val postEditUseCase: PostEditUseCase,
) {
    fun create(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(CreatePostRequest::class.java)
            .flatMap { postCreateUseCase.create(it.toCommand(), request.memberId()) }
            .map { CreatePostResponse.from(it) }
            .flatMap(::okWithBody)

    data class CreatePostRequest(
        val title: String,
        val content: String,
        val choices: List<CreateChoiceRequest>,
    ) {
        data class CreateChoiceRequest(
            val name: String,
        )

        fun toCommand(): PostCreateUseCase.Command =
            PostCreateUseCase.Command(title, content, choices.map { PostCreateUseCase.Command.ChoiceCommand(it.name) })

    }

    data class CreatePostResponse(
        val id: Long,
        val memberId: Long,
        val title: String,
        val content: String,
        val choices: List<Choice>,
    ) {
        companion object {
            fun from(result: PostCreateUseCase.Result): CreatePostResponse =
                CreatePostResponse(
                    result.id,
                    result.memberId,
                    result.title,
                    result.content,
                    result.choices.map { Choice(it.id, it.sequence, it.name) })
        }

        data class Choice(
            val id: Long,
            val sequence: Int,
            val name: String,
        )
    }

    fun edit(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(EditPostRequest::class.java)
            .flatMap { postEditUseCase.edit(request.pathVariable("id").toLong(), it.toCommand(), request.memberId()) }
            .map { EditPostResponse.from(it) }
            .flatMap(::okWithBody)

    data class EditPostRequest(
        val title: String,
        val content: String,
        val choices: List<EditChoiceRequest>,
    ) {
        data class EditChoiceRequest(
            val id: Long,
            val sequence: Int,
            val name: String,
        )

        fun toCommand(): PostEditUseCase.Command =
            PostEditUseCase.Command(
                title,
                content,
                choices.map { PostEditUseCase.Command.ChoiceCommand(it.id, it.sequence, it.name) })

    }

    data class EditPostResponse(
        val id: Long,
        val memberId: Long,
        val title: String,
        val content: String,
        val choices: List<Choice>,
    ) {
        companion object {
            fun from(result: PostEditUseCase.Result): EditPostResponse =
                EditPostResponse(
                    result.id,
                    result.memberId,
                    result.title,
                    result.content,
                    result.choices.map { Choice(it.id, it.sequence, it.name) })
        }

        data class Choice(
            val id: Long,
            val sequence: Int,
            val name: String,
        )
    }

}
