package teamversus.naenio.api.domain.choice.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.domain.choice.application.ChoiceDeleteUseCase
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository

@Service
class ChoiceService(private val choiceRepository: ChoiceRepository) :
    ChoiceCreateUseCase, ChoiceDeleteUseCase {
    override fun create(commands: List<ChoiceCreateUseCase.Command>): Flux<ChoiceCreateUseCase.Result> =
        require(commands.size == 2) { "선택지의 숫자는 두개여야 합니다." }
            .run {
                choiceRepository.saveAll(commands.map { it.toDomain() })
                    .map { ChoiceCreateUseCase.Result(it.id, it.postId, it.sequence, it.name) }
            }

    @Transactional
    override fun deleteAllByPostId(id: Long): Mono<Void> =
        choiceRepository.deleteAllByPostId(id)
}