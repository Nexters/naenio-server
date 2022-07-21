package teamversus.naenio.api.domain.member.application

import reactor.core.publisher.Mono

interface MemberSetNicknameUseCase {
    fun set(nickname: String, memberId: Long): Mono<Response>

    data class Response(val nickname: String)
}
