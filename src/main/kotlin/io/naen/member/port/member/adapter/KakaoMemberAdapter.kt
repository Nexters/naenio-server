package io.naen.member.port.member.adapter

import io.naen.member.domain.model.AuthServiceType
import io.naen.member.port.member.ExternalMemberLoadPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me"
private const val BEARER = "Bearer"

@Component
class KakaoMemberAdapter : ExternalMemberLoadPort {
    override fun findBy(authToken: String): Mono<ExternalMemberLoadPort.Response> {

        return WebClient.builder()
            .baseUrl(KAKAO_USER_ME_URL)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "$BEARER $authToken")
            .build()
            .post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .bodyToMono(UserResponse::class.java)
            .map { ExternalMemberLoadPort.Response(it.id, AuthServiceType.KAKAO) }
    }

    override fun support(authServiceType: AuthServiceType): Boolean = AuthServiceType.KAKAO == authServiceType

    data class UserResponse(val id: String)
}