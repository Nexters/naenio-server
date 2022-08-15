package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.query.model.SortType
import teamversus.naenio.api.query.result.AppFeedSortTypeQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class AppFeedSortTypeFetcher {
    fun findAll(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(SortType.values())
            .map { AppFeedSortTypeQueryResult(it.toList()) }
            .flatMap(::okWithBody)
}
