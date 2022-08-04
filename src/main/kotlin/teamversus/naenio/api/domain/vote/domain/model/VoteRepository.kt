package teamversus.naenio.api.domain.vote.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface VoteRepository : ReactiveCrudRepository<Vote, Long> {
    fun existsByChoiceIdAndMemberId(choiceId: Long, memberId: Long): Mono<Boolean>

    fun countByChoiceId(choiceId: Long): Mono<Long>
}