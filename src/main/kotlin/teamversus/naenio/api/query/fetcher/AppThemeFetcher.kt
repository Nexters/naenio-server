package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.query.model.Theme
import teamversus.naenio.api.query.result.AppThemesQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class AppThemeFetcher {
    fun findAll(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(Theme.values())
            .map { AppThemesQueryResult(it.toList()) }
            .flatMap(::okWithBody)
}
