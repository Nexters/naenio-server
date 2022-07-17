package teamversus.naenio.api.command.choice.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import teamversus.naenio.api.command.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.command.choice.application.ChoiceEditUseCase
import teamversus.naenio.api.command.choice.domain.model.ChoiceRepository

@Service
class ChoiceService(private val choiceRepository: ChoiceRepository) :
    ChoiceCreateUseCase, ChoiceEditUseCase {
    override fun create(commands: List<ChoiceCreateUseCase.Command>): Flux<ChoiceCreateUseCase.Result> =
        choiceRepository.saveAll(commands.map { it.toDomain() })
            .map { ChoiceCreateUseCase.Result(it.id, it.postId, it.sequence, it.name) }

    override fun edit(commands: List<ChoiceEditUseCase.Command>): Flux<ChoiceEditUseCase.Result> =
        choiceRepository.findAllById(commands.map { it.id })
            .switchIfEmpty(Mono.error(IllegalArgumentException("존재하지 않는 선택지 choiceId=${commands.map { it.id }}")))
            .flatMap { choiceRepository.saveAll(commands.map { it.toDomain() }) }
            .map { ChoiceEditUseCase.Result(it.id, it.postId, it.sequence, it.name) }
}