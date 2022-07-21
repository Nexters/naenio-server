package teamversus.naenio.api.domain.member.port.member.adapter

import io.jsonwebtoken.Jwts
import org.springframework.boot.json.GsonJsonParser
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.domain.member.port.member.ExternalMemberLoadPort
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.util.*

private const val APPLE_AUTH_KEYS_URL = "https://appleid.apple.com/auth/keys"
private const val KID = "kid"
private const val ALG = "alg"
private val BASE_64_DECODER = Base64.getDecoder()
private val BASE_64_URL_DECODER = Base64.getUrlDecoder()

@Component
class AppleMemberAdapter : ExternalMemberLoadPort {
    override fun findBy(authToken: String): Mono<ExternalMemberLoadPort.Response> {
        val clientTokenInfo = GsonJsonParser().parseMap(String(BASE_64_DECODER.decode(authToken.split(".")[0])))
        val clientKid = clientTokenInfo[KID] as String
        val clientAlg = clientTokenInfo[ALG] as String

        return WebClient.builder()
            .baseUrl(APPLE_AUTH_KEYS_URL)
            .build()
            .get()
            .retrieve()
            .bodyToMono(AuthKeysResponse::class.java)
            .map { toMatchedKey(it.keys, clientKid, clientAlg) }
            .map { toResponse(it, authToken) }
    }

    private fun toMatchedKey(keys: List<AuthKeysResponse.AuthKey>, clientKid: String, clientAlg: String) =
        keys.find { it.kid == clientKid && it.alg == clientAlg } ?: throw IllegalArgumentException("일치하는 공개키 없음")

    private fun toResponse(it: AuthKeysResponse.AuthKey, authToken: String) =
        ExternalMemberLoadPort.Response(
            Jwts.parser()
                .setSigningKey(
                    KeyFactory.getInstance("RSA")
                        .generatePublic(
                            RSAPublicKeySpec(
                                BigInteger(1, BASE_64_URL_DECODER.decode(it.n)),
                                BigInteger(1, BASE_64_URL_DECODER.decode(it.e))
                            )
                        )
                )
                .parseClaimsJws(authToken).body.subject, AuthServiceType.APPLE
        )

    override fun support(authServiceType: AuthServiceType): Boolean = AuthServiceType.APPLE == authServiceType

    data class AuthKeysResponse(val keys: List<AuthKey>) {
        data class AuthKey(
            val kty: String,
            val kid: String,
            val use: String,
            val alg: String,
            val n: String,
            val e: String,
        )
    }
}