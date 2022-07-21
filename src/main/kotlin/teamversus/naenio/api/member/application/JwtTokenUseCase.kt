package teamversus.naenio.api.member.application

interface JwtTokenUseCase {
    fun createToken(id: Long): String

    fun extractMemberId(token: String): Long
}
