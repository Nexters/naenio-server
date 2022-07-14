package teamversus.naenio.api.member.web.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.member.application.LoginUseCase
import teamversus.naenio.api.member.application.MemberSetNicknameUseCase
import teamversus.naenio.api.member.domain.model.AuthServiceType
import teamversus.naenio.api.member.web.handler.support.okWithBody


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
            .map { SetNicknameResponse(it.nickname) }
            .flatMap(::okWithBody)

    data class SetNicknameRequest(val nickname: String)

    data class SetNicknameResponse(val nickname: String)
}
