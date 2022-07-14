package io.naen.api.member.port.member

import io.naen.api.member.domain.model.AuthServiceType
import io.naen.api.member.domain.model.Member
import reactor.core.publisher.Mono

interface ExternalMemberLoadPort {
    fun findBy(authToken: String): Mono<Response>

    fun support(authServiceType: AuthServiceType): Boolean

    data class Response(val authId: String, val authServiceType: AuthServiceType) {
        fun toDomain(): Member = Member(authId = authId, authServiceType = authServiceType, nickname = null)
    }
}

