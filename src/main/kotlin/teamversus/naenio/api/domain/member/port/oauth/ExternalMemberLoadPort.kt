package teamversus.naenio.api.domain.member.port.oauth

import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.domain.member.domain.model.Member

interface ExternalMemberLoadPort {
    fun findBy(authToken: String): Mono<Response>

    fun support(authServiceType: AuthServiceType): Boolean

    data class Response(val authId: String, val authServiceType: AuthServiceType) {
        fun toDomain(): Member =
            Member(authId = authId, authServiceType = authServiceType, nickname = null, profileImageIndex = null)
    }
}

