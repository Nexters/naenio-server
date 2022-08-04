package teamversus.naenio.api.domain.member.application

import reactor.core.publisher.Mono

interface MemberWithdrawUseCase {
    fun withdraw(id: Long): Mono<Void>
}
