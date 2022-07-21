package teamversus.naenio.api.member.application

import reactor.core.publisher.Mono
import teamversus.naenio.api.member.domain.model.AuthServiceType

interface LoginUseCase {
    fun login(authToken: String, authServiceType: AuthServiceType): Mono<LoginResult>

    data class LoginResult(val token: String)
}
