package io.naen.api.member.web.handler

import io.naen.api.filter.memberId
import io.naen.api.member.application.LoginUseCase
import io.naen.api.member.application.MemberSetNicknameUseCase
import io.naen.api.member.domain.model.AuthServiceType
import io.naen.api.member.web.handler.support.okWithBody
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@Component
class MemberHandler(
    private val loginUseCase: LoginUseCase,
    private val memberSetNicknameUseCase: MemberSetNicknameUseCase,
) {
    fun login(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(LoginRequest::class.java)
            .flatMap { loginUseCase.login(it.authToken, it.authServiceType) }
            .map { LoginResponse.of(it) }
            .flatMap(::okWithBody)

    data class LoginRequest(val authToken: String, val authServiceType: AuthServiceType)

    data class LoginResponse(val token: String) {
        companion object {
            fun of(result: LoginUseCase.LoginResult): LoginResponse = LoginResponse(result.token)
        }
    }

    fun setNickname(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(SetNicknameRequest::class.java)
            .flatMap { memberSetNicknameUseCase.set(it.nickname, request.memberId()) }
            .flatMap(::okWithBody)

    data class SetNicknameRequest(val nickname: String)
}
