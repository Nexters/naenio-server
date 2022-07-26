package teamversus.naenio.api.domain.member.port.oauth.adapter

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.domain.member.port.oauth.ExternalMemberLoadPort

@Profile("!local")
@Component
class GoogleMemberAdapter(@Value("\${auth.google.client-id}") private val clientId: String) : ExternalMemberLoadPort {
    private val googleIdTokenVerifier: GoogleIdTokenVerifier =
        GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(listOf(clientId))
            .build()

    override fun findBy(authToken: String): Mono<ExternalMemberLoadPort.Response> =
        Mono.just(
            ExternalMemberLoadPort.Response(
                googleIdTokenVerifier.verify(authToken).payload.subject,
                AuthServiceType.GOOGLE
            )
        )

    override fun support(authServiceType: AuthServiceType): Boolean =
        authServiceType == AuthServiceType.GOOGLE


}