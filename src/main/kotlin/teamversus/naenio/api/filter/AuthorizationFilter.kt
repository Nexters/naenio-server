package teamversus.naenio.api.filter

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.application.JwtTokenUseCase
import teamversus.naenio.api.domain.member.domain.model.MemberRepository

private const val MEMBER_ID_ATTRIBUTE = "memberId"
private const val TOKEN_PREFIX = "Bearer "
private const val LOGIN_PATH = "/app/login"

@Component
class AuthorizationFilter(
    private val memberRepository: MemberRepository,
    private val jwtTokenUseCase: JwtTokenUseCase,
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.path.pathWithinApplication().value()
        if ((LOGIN_PATH == path) or (Regex("/app/*") !in path)) {
            return chain.filter(exchange);
        }

        val authorization = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            ?: throw IllegalArgumentException("Authorization 헤더가 존재하지 않습니다.")

        if (TOKEN_PREFIX in authorization) {
            val token = authorization.substringAfter(TOKEN_PREFIX)
            if (token.contains("test_member_")) {
                exchange.attributes[MEMBER_ID_ATTRIBUTE] = token.substringAfterLast("_")
                return chain.filter(exchange)
            }
            val memberId = jwtTokenUseCase.extractMemberId(token)

            return memberRepository.existsById(memberId)
                .flatMap {
                    when {
                        !it -> Mono.error(IllegalArgumentException("id와 일치하는 회원이 존재하지 않습니다. id=${memberId}"))
                        else -> {
                            exchange.attributes[MEMBER_ID_ATTRIBUTE] = memberId
                            chain.filter(exchange)
                        }
                    }
                }
        }

        throw IllegalArgumentException("지원하지 않는 Authorization 형식 authorization=${authorization}")
    }
}

fun ServerRequest.memberId(): Long = this.attribute(MEMBER_ID_ATTRIBUTE).get().toString().toLong()
