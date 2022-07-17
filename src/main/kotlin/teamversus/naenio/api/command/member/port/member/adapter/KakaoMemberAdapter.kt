package teamversus.naenio.api.command.member.port.member.adapter

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import teamversus.naenio.api.command.member.domain.model.AuthServiceType
import teamversus.naenio.api.command.member.port.member.ExternalMemberLoadPort

private const val KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me"
private const val TOKEN_PREFIX = "Bearer"

@Component
class KakaoMemberAdapter : ExternalMemberLoadPort {
    override fun findBy(authToken: String): Mono<ExternalMemberLoadPort.Response> =
        WebClient.builder()
            .baseUrl(KAKAO_USER_ME_URL)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "$TOKEN_PREFIX $authToken")
            .build()
            .post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .bodyToMono(UserResponse::class.java)
            .map { ExternalMemberLoadPort.Response(it.id, AuthServiceType.KAKAO) }

    override fun support(authServiceType: AuthServiceType): Boolean = AuthServiceType.KAKAO == authServiceType

    data class UserResponse(val id: String)
}