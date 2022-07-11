package io.naen.member.web.handler

import io.naen.member.application.LoginUseCase
import io.naen.member.domain.model.AuthServiceType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@Component
class MemberHandler(private val loginUseCase: LoginUseCase) {
    fun login(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Request::class.java)
            .flatMap { loginUseCase.login(it.authToken, it.authServiceType) }
            .map { Response.of(it) }
            .flatMap {
                ServerResponse.ok()
                    .body(BodyInserters.fromValue(it))
            }

    data class Request(val authToken: String, val authServiceType: AuthServiceType)

    data class Response(val token: String) {
        companion object {
            fun of(result: LoginUseCase.LoginResult): Response = Response(result.token)
        }
    }
}
