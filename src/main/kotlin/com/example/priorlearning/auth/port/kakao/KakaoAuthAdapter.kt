package com.example.priorlearning.auth.port.kakao

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

private const val KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token"
private const val KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me"
private const val GRANT_TYPE = "authorization_code"
private const val CLIENT_ID = "clientId"
private const val REDIRECT_URI = "http://localhost:8080/login"
private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

@Component
class KakaoAuthAdapter : KakaoTokenLoadPort, KakaoUserLoadPort {
    override fun findKakaoAccessToken(code: String): Mono<KakaoTokenLoadPort.Response> {
        val baseUrl = UriComponentsBuilder.fromHttpUrl(KAKAO_OAUTH_TOKEN_URL)
            .queryParam("client_id", CLIENT_ID)
            .queryParam("grant_type", GRANT_TYPE)
            .queryParam("redirect_uri", REDIRECT_URI)
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

    override fun findBy(accessToken: String): Mono<KakaoUserLoadPort.Response> {
        return WebClient.builder()
            .baseUrl(KAKAO_USER_ME_URL)
            .defaultHeader(AUTHORIZATION, "$BEARER $accessToken")
            .build()
            .post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .bodyToMono(UserResponse::class.java)
            .map { KakaoUserLoadPort.Response(it.id) }
    }

    data class UserResponse(val id: Long)
}