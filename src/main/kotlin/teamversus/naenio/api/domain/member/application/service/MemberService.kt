package teamversus.naenio.api.domain.member.application.service

import io.r2dbc.spi.R2dbcDataIntegrityViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.member.application.JwtTokenUseCase
import teamversus.naenio.api.domain.member.application.LoginUseCase
import teamversus.naenio.api.domain.member.application.MemberExistByNicknameUseCase
import teamversus.naenio.api.domain.member.application.MemberSetNicknameUseCase
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.member.port.oauth.ExternalMemberLoadPort


private const val DUPLICATE_ENTRY_CODE = 1062

@Service
class MemberService(
    private val externalMemberLoadPorts: List<ExternalMemberLoadPort>,
    private val memberRepository: MemberRepository,
    private val jwtTokenUseCase: JwtTokenUseCase,
) : LoginUseCase, MemberSetNicknameUseCase, MemberExistByNicknameUseCase {
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

    override fun set(nickname: String, memberId: Long): Mono<MemberSetNicknameUseCase.Response> =
        memberRepository.findById(memberId)
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 회원 memberId=${memberId}")) }
            .map { it.changeNickname(nickname) }
            .flatMap {
                memberRepository.save(it)
                    .onErrorMap(this::isDuplicateEntryError) { IllegalArgumentException("중복된 닉네임") }
            }
            .map { MemberSetNicknameUseCase.Response(it.nickname!!) }

    override fun exist(nickname: String): Mono<Boolean> =
        memberRepository.existsByNickname(nickname)

    private fun isDuplicateEntryError(t: Throwable): Boolean =
        t is DataIntegrityViolationException && (t.cause as R2dbcDataIntegrityViolationException).errorCode == DUPLICATE_ENTRY_CODE
}
