package teamversus.naenio.api.domain.report.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.report.domain.model.Report
import teamversus.naenio.api.domain.report.domain.model.ReportResource
import teamversus.naenio.api.domain.report.domain.model.ReportResourceRepository
import teamversus.naenio.api.filter.memberId

@Component
class ReportHandler(private val reportResourceRepository: ReportResourceRepository) {

    fun register(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(RegisterReportRequest::class.java)
            .flatMap { reportResourceRepository.save(Report(0, request.memberId(), it.targetMemberId, it.resource)) }
            .flatMap { ServerResponse.noContent().build() }

    data class RegisterReportRequest(
        val targetMemberId: Long,
        val resource: ReportResource,
    )
}
