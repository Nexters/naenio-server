package com.example.priorlearning.auth.web.handler

import com.example.priorlearning.auth.application.LoginUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

@Component
class AuthHandler(private val loginUseCase: LoginUseCase) {
    fun login(request: ServerRequest): Mono<ServerResponse> =
        loginUseCase.login(request.queryParam("code")
            .orElseThrow { IllegalArgumentException() })
            .map { Response.of(it) }
            .flatMap {
                ServerResponse.permanentRedirect(URI.create("/"))
                    .body(BodyInserters.fromValue(it))
            }

    data class Response(val id: Long) {
        companion object {
            fun of(result: LoginUseCase.LoginResult): Response = Response(result.id)
        }
    }
}
