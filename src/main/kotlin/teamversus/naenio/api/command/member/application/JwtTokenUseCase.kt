package teamversus.naenio.api.command.member.application

interface JwtTokenUseCase {
    fun createToken(id: Long): String

    fun extractMemberId(token: String): Long
}
