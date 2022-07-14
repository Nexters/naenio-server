package teamversus.naenio.api.member.application.service

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import teamversus.naenio.api.member.application.JwtTokenUseCase
import java.time.Duration
import java.util.*

private const val ISSUER = "naenio"
private const val SUBJECT = "auth"
private const val DAYS_OF_YEAR = 365L

private const val ID = "id"

@Service
class JwtTokenService(@Value("\${auth.jwt.secret}") private val secret: String) : JwtTokenUseCase {
    override fun createToken(id: Long): String {
        val now = Date()

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(ISSUER)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + Duration.ofDays(DAYS_OF_YEAR).toMillis()))
            .setSubject(SUBJECT)
            .setClaims(mapOf(ID to id))
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.toByteArray()))
            .compact()
    }

    override fun extractMemberId(token: String): Long =
        Jwts.parser()
            .setSigningKey(Base64.getEncoder().encodeToString(secret.toByteArray()))
            .parseClaimsJwt(token)
            .body[ID] as Long
}