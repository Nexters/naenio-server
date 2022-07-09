package io.naen.member.application

import io.naen.member.domain.model.MemberRepository
import io.naen.member.port.kakao.KakaoMemberLoadPort
import io.naen.member.port.kakao.KakaoTokenLoadPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class LoginService(
    private val kakaoTokenLoadPort: KakaoTokenLoadPort,
    private val kakaoMemberLoadPort: KakaoMemberLoadPort,
    private val memberRepository: MemberRepository,
) : LoginUseCase {
    override fun login(code: String): Mono<LoginUseCase.LoginResult> =
        kakaoTokenLoadPort.findKakaoAccessToken(code)
            .flatMap { kakaoMemberLoadPort.findBy(it.accessToken) }
            .flatMap {
                memberRepository.findByOauthId(it.oAuthId.toString())
                    .switchIfEmpty { memberRepository.save(it.toDomain()) }
            }
            .map { LoginUseCase.LoginResult(it.id, it.nickname) }
}