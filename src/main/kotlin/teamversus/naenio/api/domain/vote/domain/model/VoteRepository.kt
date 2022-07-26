package teamversus.naenio.api.domain.vote.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface VoteRepository : ReactiveCrudRepository<Vote, Long>