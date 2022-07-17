package teamversus.naenio.api.core.post.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.core.choice.application.ChoiceEditUseCase
import teamversus.naenio.api.core.post.domain.model.Post

interface PostEditUseCase {
    fun edit(id: Long, command: Command, memberId: Long): Mono<Result>

    data class Command(
        val title: String,
        val content: String,
        val choices: List<ChoiceCommand>,
    ) {
        data class ChoiceCommand(
            val id: Long,
            val sequence: Int,
            val name: String,
        )

        fun toDomain(id: Long, memberId: Long): Post = Post(id, memberId, title, content)

        fun toChoiceEditCommands(postId: Long): List<ChoiceEditUseCase.Command> =
            choices.map {
                ChoiceEditUseCase.Command(
                    it.id,
                    postId,
                    it.sequence,
                    it.name
                )
            }
    }

    data class Result(
        val id: Long,
        val memberId: Long,
        val title: String,
        val content: String,
        val choices: List<Choice>,
    ) {
        data class Choice(
            val id: Long,
            val sequence: Int,
            val name: String,
        )
    }
}
