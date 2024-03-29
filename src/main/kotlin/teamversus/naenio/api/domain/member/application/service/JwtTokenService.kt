package teamversus.naenio.api.domain.member.application.service

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import teamversus.naenio.api.domain.member.application.JwtTokenUseCase
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

    override fun extractMemberId(token: String): Long {
        try {
            return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.toByteArray()))
                .parseClaimsJws(token)
                .body[ID].toString().toLong()
        } catch (e: Exception) {
            log.error("fail extract memberId. message=${e.message}", e)
            throw TokenException()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}