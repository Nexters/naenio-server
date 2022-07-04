package com.example.priorlearning.auth.port.kakao

import reactor.core.publisher.Mono

interface KakaoTokenLoadPort {
    fun findKakaoAccessToken(code: String): Mono<Response>

    data class Response(
        val accessToken: String,
    )
}