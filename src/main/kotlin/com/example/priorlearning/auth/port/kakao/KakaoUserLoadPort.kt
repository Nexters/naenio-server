package com.example.priorlearning.auth.port.kakao

import reactor.core.publisher.Mono

interface KakaoUserLoadPort {
    fun findBy(accessToken: String): Mono<Response>

    data class Response(val id: Long)
}
