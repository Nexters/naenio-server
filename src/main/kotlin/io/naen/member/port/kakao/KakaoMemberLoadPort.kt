package io.naen.member.port.kakao

import io.naen.member.domain.model.Member
import reactor.core.publisher.Mono

interface KakaoMemberLoadPort {
    fun findBy(accessToken: String): Mono<Response>

    data class Response(val oAuthId: Long, val nickname: String) {
        fun toDomain(): Member =
            Member(oauthId = oAuthId.toString(), nickname = nickname)
    }
}
