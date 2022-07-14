package io.naen.api.member.application

import io.naen.api.member.domain.model.AuthServiceType
import reactor.core.publisher.Mono

interface LoginUseCase {
    fun login(authToken: String, authServiceType: AuthServiceType): Mono<LoginResult>

    data class LoginResult(val token: String)
}
