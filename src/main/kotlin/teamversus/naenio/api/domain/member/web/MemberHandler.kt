package teamversus.naenio.api.domain.member.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.application.LoginUseCase
import teamversus.naenio.api.domain.member.application.MemberExistByNicknameUseCase
import teamversus.naenio.api.domain.member.application.MemberSetNicknameUseCase
import teamversus.naenio.api.domain.member.application.MemberSetProfileImageUseCase
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.support.okWithBody


@Component
class MemberHandler(
    private val loginUseCase: LoginUseCase,
    private val memberSetNicknameUseCase: MemberSetNicknameUseCase,
    private val memberExistByNicknameUseCase: MemberExistByNicknameUseCase,
    private val memberSetProfileImageUseCase: MemberSetProfileImageUseCase,
) {
    fun login(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(LoginRequest::class.java)
            .flatMap { loginUseCase.login(it.authToken, it.authServiceType) }
            .map { LoginResponse.from(it) }
            .flatMap(::okWithBody)

    data class LoginRequest(val authToken: String, val authServiceType: AuthServiceType)

    data class LoginResponse(
        val token: String,
        val authServiceType: AuthServiceType,
        val nickname: String?,
        val profileImageIndex: Int?,
    ) {
        companion object {
            fun from(result: LoginUseCase.LoginResult): LoginResponse =
                LoginResponse(
                    result.token,
                    result.authServiceType,
                    result.nickname,
                    result.profileImageIndex
                )
        }
    }

    fun setNickname(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(SetNicknameRequest::class.java)
            .flatMap { memberSetNicknameUseCase.setNickname(it.nickname, request.memberId()) }
            .map { SetNicknameResponse(it.nickname) }
            .flatMap(::okWithBody)

    data class SetNicknameRequest(val nickname: String)

    data class SetNicknameResponse(val nickname: String)

    fun setProfileImage(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(SetProfileImageRequest::class.java)
            .flatMap { memberSetProfileImageUseCase.setProfileImageIndex(it.profileImageIndex, request.memberId()) }
            .map { SetProfileImageResponse(it.profileImageIndex) }
            .flatMap(::okWithBody)

    data class SetProfileImageRequest(val profileImageIndex: Int)

    data class SetProfileImageResponse(val profileImageIndex: Int)

    fun exist(request: ServerRequest): Mono<ServerResponse> =
        memberExistByNicknameUseCase.exist(
            request.queryParam("nickname")
                .orElseThrow { IllegalArgumentException("닉네임은 필수값 입니다.") })
            .map { ExistResponse(it) }
            .flatMap(::okWithBody)

    data class ExistResponse(val exist: Boolean)
}
