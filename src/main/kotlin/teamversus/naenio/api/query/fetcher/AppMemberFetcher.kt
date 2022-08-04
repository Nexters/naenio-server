package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.query.result.AppMemberQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class AppMemberFetcher(private val memberRepository: MemberRepository) {
    fun findMe(request: ServerRequest): Mono<ServerResponse> =
        memberRepository.findById(request.memberId())
            .map { AppMemberQueryResult(it.id, it.authServiceType, it.nickname, it.profileImageIndex) }
            .flatMap { okWithBody(it) }
}
