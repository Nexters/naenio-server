package io.naen.auth.port.kakao

import io.naen.auth.domain.model.Member
import reactor.core.publisher.Mono

interface KakaoMemberLoadPort {
    fun findBy(accessToken: String): Mono<Response>

    data class Response(val oAuthId: Long, val nickname: String) {
        fun toDomain(): Member =
            Member(oauthId = oAuthId.toString(), nickname = nickname)
    }
}
