package com.example.priorlearning.auth.application

import com.example.priorlearning.auth.port.kakao.KakaoTokenLoadPort
import com.example.priorlearning.auth.port.kakao.KakaoUserLoadPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LoginService(
    private val kakaoTokenLoadPort: KakaoTokenLoadPort,
    private val kakaoUserLoadPort: KakaoUserLoadPort,
) : LoginUseCase {
    override fun login(code: String): Mono<LoginUseCase.LoginResult> =
        kakaoTokenLoadPort.findKakaoAccessToken(code)
            .flatMap { kakaoUserLoadPort.findBy(it.accessToken) }
            .map { LoginUseCase.LoginResult(it.id) }
            .doOnNext { log.info("id=${it.id}") }

    companion object {
        private val log = LoggerFactory.getLogger(LoginService::class.java.name)
    }
}