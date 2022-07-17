package teamversus.naenio.api.command.choice.application

import reactor.core.publisher.Flux
import teamversus.naenio.api.command.choice.domain.model.Choice

interface ChoiceEditUseCase {
    fun edit(commands: List<Command>): Flux<Result>

    data class Command(
        val id: Long,
        val postId: Long,
        val sequence: Int,
        val name: String,
    ) {
        fun toDomain(): Choice = Choice(id, postId, sequence, name)
    }

    data class Result(
        val id: Long,
        val postId: Long,
        val sequence: Int,
        val name: String,
    )
}
