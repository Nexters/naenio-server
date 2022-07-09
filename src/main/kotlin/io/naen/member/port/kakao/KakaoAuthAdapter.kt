package io.naen.member.port.kakao

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

private const val KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token"
private const val KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me"
private const val GRANT_TYPE = "authorization_code"
private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

@Component
class KakaoAuthAdapter(
    @Value("\${oauth.kakao.client-id}") private val clientId: String,
    @Value("\${oauth.redirect-uri}") private val redirectUri: String,
) : KakaoTokenLoadPort, KakaoMemberLoadPort {
    override fun findKakaoAccessToken(code: String): Mono<KakaoTokenLoadPort.Response> {
        val baseUrl = UriComponentsBuilder.fromHttpUrl(KAKAO_OAUTH_TOKEN_URL)
            .queryParam("client_id", clientId)
            .queryParam("grant_type", GRANT_TYPE)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("code", code)
            .toUriString()

        return WebClient.builder()
            .baseUrl(baseUrl)
            .build()
            .get()
            .retrieve()
            .bodyToMono(TokenResponse::class.java)
            .map { KakaoTokenLoadPort.Response(it.access_token) }
    }

    data class TokenResponse(val access_token: String, val refresh_token: String)

    override fun findBy(accessToken: String): Mono<KakaoMemberLoadPort.Response> {
        return WebClient.builder()
            .baseUrl(KAKAO_USER_ME_URL)
            .defaultHeader(AUTHORIZATION, "$BEARER $accessToken")
            .build()
            .post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .bodyToMono(UserResponse::class.java)
            .map { KakaoMemberLoadPort.Response(it.id, it.properties.nickname) }
    }

    data class UserResponse(val id: Long, val properties: Properties) {
        data class Properties(val nickname: String)
    }
}