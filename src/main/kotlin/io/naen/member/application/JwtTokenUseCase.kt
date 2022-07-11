package io.naen.member.application

interface JwtTokenUseCase {
    fun createToken(id: Long): String
}
