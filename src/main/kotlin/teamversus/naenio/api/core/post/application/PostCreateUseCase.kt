package teamversus.naenio.api.core.post.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.core.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.core.post.domain.model.Post

interface PostCreateUseCase {
    fun create(command: Command, memberId: Long): Mono<Result>

    data class Command(
        val title: String,
        val content: String,
        val choices: List<ChoiceCommand>,
    ) {
        data class ChoiceCommand(
            val name: String,
        )

        fun toPost(memberId: Long): Post =
            Post(0, memberId, title, content)

        fun toChoiceCreateCommands(postId: Long): List<ChoiceCreateUseCase.Command> =
            choices.mapIndexed { index, choiceCommand ->
                ChoiceCreateUseCase.Command(
                    postId,
                    index,
                    choiceCommand.name
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
            val postId: Long,
            val sequence: Int,
            val name: String,
        )
    }
}
