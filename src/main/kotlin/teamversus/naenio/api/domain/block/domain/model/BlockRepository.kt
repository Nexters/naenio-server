package teamversus.naenio.api.domain.block.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface BlockRepository : ReactiveCrudRepository<Block, Long> {
    fun findAllByMemberId(memberId: Long): Flux<Block>
}