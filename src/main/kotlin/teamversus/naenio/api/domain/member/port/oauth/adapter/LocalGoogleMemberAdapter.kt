package teamversus.naenio.api.domain.member.port.oauth.adapter

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.domain.model.AuthServiceType
import teamversus.naenio.api.domain.member.port.oauth.ExternalMemberLoadPort

private const val GOOGLE_OAUTH2_URL = "https://oauth2.googleapis.com"
private const val TOKEN_INFO = "/tokeninfo"
private const val ID_TOKEN = "id_token"

@Profile("local")
@Component
class LocalGoogleMemberAdapter : ExternalMemberLoadPort {
    override fun findBy(authToken: String): Mono<ExternalMemberLoadPort.Response> =
        WebClient.builder()
            .baseUrl(GOOGLE_OAUTH2_URL)
            .build()
            .get()
            .uri {
                it.path(TOKEN_INFO)
                    .queryParam(ID_TOKEN, authToken)
                    .build()
            }
            .retrieve()
            .bodyToMono(Response::class.java)
            .map { ExternalMemberLoadPort.Response(it.sub, AuthServiceType.GOOGLE) }

    data class Response(val sub: String)

    override fun support(authServiceType: AuthServiceType): Boolean = authServiceType == AuthServiceType.GOOGLE
}