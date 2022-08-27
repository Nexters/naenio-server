package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.query.model.NoticeRepository
import teamversus.naenio.api.query.result.AppNoticesQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class AppNoticeFetcher(private val noticeRepository: NoticeRepository) {
    fun findAll(request: ServerRequest): Mono<ServerResponse> =
        noticeRepository.findAll()
            .map { AppNoticesQueryResult.NoticeResult(it.id, it.title, it.content) }
            .collectList()
            .map { AppNoticesQueryResult(it) }
            .flatMap { okWithBody(it) }
}
