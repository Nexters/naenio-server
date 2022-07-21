package teamversus.naenio.api.choice.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import teamversus.naenio.api.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.choice.domain.model.ChoiceRepository

@Service
class ChoiceService(private val choiceRepository: ChoiceRepository) :
    ChoiceCreateUseCase {
    override fun create(commands: List<ChoiceCreateUseCase.Command>): Flux<ChoiceCreateUseCase.Result> =
        require(commands.size == 2) { "선택지의 숫자는 두개여야 합니다." }
            .run {
                choiceRepository.saveAll(commands.map { it.toDomain() })
                    .map { ChoiceCreateUseCase.Result(it.id, it.postId, it.sequence, it.name) }
            }
}