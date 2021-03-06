package teamversus.naenio.api.domain.member.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MemberRepository : ReactiveCrudRepository<Member, Long> {
    fun findByAuthIdAndAuthServiceType(authId: String, authServiceType: AuthServiceType): Mono<Member>
    fun existsByNickname(nickname: String): Mono<Boolean>
}