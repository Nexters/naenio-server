package teamversus.naenio.api.command.member.port.member

import reactor.core.publisher.Mono
import teamversus.naenio.api.command.member.domain.model.AuthServiceType
import teamversus.naenio.api.command.member.domain.model.Member

interface ExternalMemberLoadPort {
    fun findBy(authToken: String): Mono<Response>

    fun support(authServiceType: AuthServiceType): Boolean

    data class Response(val authId: String, val authServiceType: AuthServiceType) {
        fun toDomain(): Member = Member(authId = authId, authServiceType = authServiceType, nickname = null)
    }
}

