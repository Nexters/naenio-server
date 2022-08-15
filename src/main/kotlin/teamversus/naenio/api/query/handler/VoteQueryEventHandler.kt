package teamversus.naenio.api.query.handler

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.domain.vote.domain.event.VoteChangedEvent
import teamversus.naenio.api.domain.vote.domain.event.VoteCreatedEvent
import teamversus.naenio.api.query.model.*
import java.time.LocalDate

@Component
class VoteQueryEventHandler(
    private val postVoteCountRepository: PostVoteCountRepository,
    private val postVoteByDayRepository: PostVoteByDayRepository,
    private val postChoiceVoteCountRepository: PostChoiceVoteCountRepository,
    private val choiceRepository: ChoiceRepository,
) {
    fun handle(event: VoteCreatedEvent) {
        postVoteCountRepository.findByPostId(event.postId)
            .switchIfEmpty(Mono.just(PostVoteCount(0, event.postId, 0)))
            .flatMap { postVoteCountRepository.save(it.increaseCount()) }
            .subscribe()

        postVoteByDayRepository.findByPostIdAndAggregateDate(event.postId, LocalDate.now())
            .switchIfEmpty(Mono.just(PostVoteByDay(0, event.postId, LocalDate.now(), 0)))
            .flatMap { postVoteByDayRepository.save(it.increaseCount()) }
            .subscribe()

        postChoiceVoteCountRepository.findByPostId(event.postId)
            .flatMap {
                choiceRepository.findById(event.choiceId)
                    .map { choice -> if (choice.sequence == 0) it.increaseFirst() else it.increaseSecond() }
            }
            .switchIfEmpty(choiceRepository.findById(event.choiceId)
                .map {
                    if (it.sequence == 0) PostChoiceVoteCount(
                        0,
                        event.postId,
                        1,
                        0
                    ) else PostChoiceVoteCount(0, event.postId, 0, 1)
                }
            )
            .flatMap { postChoiceVoteCountRepository.save(it) }
            .subscribe()
    }

    fun handle(event: VoteChangedEvent) {
        postChoiceVoteCountRepository.findByPostId(event.postId)
            .flatMap {
                choiceRepository.findById(event.choiceId)
                    .map { choice -> if (choice.sequence == 0) it.moveToFirst() else it.moveToSecond() }
            }
            .flatMap { postChoiceVoteCountRepository.save(it) }
            .subscribe()
    }
}