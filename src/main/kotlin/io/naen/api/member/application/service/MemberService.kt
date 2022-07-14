package io.naen.api.member.application.service

import io.naen.api.member.application.JwtTokenUseCase
import io.naen.api.member.application.LoginUseCase
import io.naen.api.member.application.MemberSetNicknameUseCase
import io.naen.api.member.domain.model.AuthServiceType
import io.naen.api.member.domain.model.MemberRepository
import io.naen.api.member.port.member.ExternalMemberLoadPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty


@Service
class MemberService(
    private val externalMemberLoadPorts: List<ExternalMemberLoadPort>,
    private val memberRepository: MemberRepository,
    private val jwtTokenUseCase: JwtTokenUseCase,
) : LoginUseCase, MemberSetNicknameUseCase {
    override fun login(authToken: String, authServiceType: AuthServiceType): Mono<LoginUseCase.LoginResult> =
        externalMemberLoadPort(authServiceType)
            .findBy(authToken)
            .flatMap {
                memberRepository.findByAuthIdAndAuthServiceType(it.authId, it.authServiceType)
                    .switchIfEmpty { memberRepository.save(it.toDomain()) }
            }
            .map { LoginUseCase.LoginResult(jwtTokenUseCase.createToken(it.id)) }

    private fun externalMemberLoadPort(authServiceType: AuthServiceType) =
        externalMemberLoadPorts.find { it.support(authServiceType) }
            ?: throw IllegalArgumentException("미지원 타입. AuthServiceType=${authServiceType}")

    override fun set(nickname: String, memberId: Long): Mono<MemberSetNicknameUseCase.Response> {
        return memberRepository.findById(memberId)
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 회원 memberId=${memberId}")) }
            .map { it.changeNickname(nickname) }
            .flatMap {
                memberRepository.save(it)
            }
            .map { MemberSetNicknameUseCase.Response(it.nickname!!) }
    }
}