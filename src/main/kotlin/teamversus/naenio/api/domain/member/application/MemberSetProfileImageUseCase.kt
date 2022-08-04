package teamversus.naenio.api.domain.member.application

import reactor.core.publisher.Mono

interface MemberSetProfileImageUseCase {

    fun setProfileImageIndex(profileImageIndex: Int, memberId: Long): Mono<Response>

    data class Response(val profileImageIndex: Int)
}
