package io.naen.auth.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MemberRepository : ReactiveCrudRepository<Member, Long> {
    fun findByOauthId(oauthId: String): Mono<Member>
}