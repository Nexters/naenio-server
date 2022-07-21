package teamversus.naenio.api.choice.application

import reactor.core.publisher.Flux
import teamversus.naenio.api.choice.domain.model.Choice

interface ChoiceCreateUseCase {
    fun create(commands: List<Command>): Flux<Result>

    data class Command(
        val postId: Long,
        val sequence: Int,
        val name: String,
    ) {
        fun toDomain(): Choice = Choice(0, postId, sequence, name)
    }

    data class Result(
        val id: Long,
        val postId: Long,
        val sequence: Int,
        val name: String,
    )
}
