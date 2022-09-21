package teamversus.naenio.api.domain.block.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.block.domain.model.Block
import teamversus.naenio.api.domain.block.domain.model.BlockRepository
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository

@Service
class BlockService(
    private val blockRepository: BlockRepository,
    private val voteRepository: VoteRepository,
) {
    @Transactional
    fun block(memberId: Long, targetMemberId: Long): Mono<Void> {
        require(memberId != targetMemberId) { "자기 자신을 차단하는 것은 불가능합니다." }

        return blockRepository.save(Block(0, memberId, targetMemberId))
            .flatMap { voteRepository.deleteAllByMemberIdAndPostAuthorId(memberId, targetMemberId) }
    }
}