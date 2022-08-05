package teamversus.naenio.api.domain.member.application.service

import io.r2dbc.spi.R2dbcDataIntegrityViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.member.application.*
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.member.port.oauth.ExternalMemberLoadPort


private const val DUPLICATE_ENTRY_CODE = 1062

@Service
class MemberService(
    private val externalMemberLoadPorts: List<ExternalMemberLoadPort>,
    private val memberRepository: MemberRepository,
    private val jwtTokenUseCase: JwtTokenUseCase,
) : LoginUseCase,
    MemberSetNicknameUseCase,
    MemberExistByNicknameUseCase,
    MemberSetProfileImageUseCase,
    MemberWithdrawUseCase {
    override fun login(authToken: String, authServiceType: AuthServiceType): Mono<LoginUseCase.LoginResult> =
        externalMemberLoadPort(authServiceType)
            .findBy(authToken)
            .flatMap {
                memberRepository.findByAuthIdAndAuthServiceType(it.authId, it.authServiceType)
                    .switchIfEmpty { memberRepository.save(it.toDomain()) }
            }
            .map {
                LoginUseCase.LoginResult(
                    jwtTokenUseCase.createToken(it.id),
                    it.authServiceType,
                    it.nickname,
                    it.profileImageIndex
                )
            }

    private fun externalMemberLoadPort(authServiceType: AuthServiceType) =
        externalMemberLoadPorts.find { it.support(authServiceType) }
            ?: throw IllegalArgumentException("미지원 타입. AuthServiceType=${authServiceType}")

    override fun setNickname(nickname: String, memberId: Long): Mono<MemberSetNicknameUseCase.Response> =
        memberRepository.findById(memberId)
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 회원 memberId=${memberId}")) }
            .map { it.changeNickname(nickname) }
            .flatMap {
                memberRepository.save(it)
                    // TODO: 2022/07/26 에러 코드 나눠야함
                    .onErrorMap(this::isDuplicateEntryError) { IllegalArgumentException("중복된 닉네임") }
            }
            .map { MemberSetNicknameUseCase.Response(it.nickname!!) }

    override fun setProfileImageIndex(
        profileImageIndex: Int,
        memberId: Long,
    ): Mono<MemberSetProfileImageUseCase.Response> =
        memberRepository.findById(memberId)
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 회원 memberId=${memberId}")) }
            .map { it.changeProfileImage(profileImageIndex) }
            .flatMap { memberRepository.save(it) }
            .map { MemberSetProfileImageUseCase.Response(it.profileImageIndex!!) }


    override fun exist(nickname: String): Mono<Boolean> =
        memberRepository.existsByNickname(nickname)

    private fun isDuplicateEntryError(t: Throwable): Boolean =
        t is DataIntegrityViolationException && (t.cause as R2dbcDataIntegrityViolationException).errorCode == DUPLICATE_ENTRY_CODE

    @Transactional
    override fun withdraw(id: Long): Mono<Void> =
        Mono.just(id)
            .filterWhen { memberRepository.existsById(it) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 회원 입니다. id=${id}}")) }
            .flatMap { memberRepository.deleteById(id) }
}
