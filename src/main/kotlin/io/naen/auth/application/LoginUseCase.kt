package io.naen.auth.application

import reactor.core.publisher.Mono

interface LoginUseCase {
    fun login(code: String): Mono<LoginResult>

    data class LoginResult(val id: Long, val nickname: String)
}
