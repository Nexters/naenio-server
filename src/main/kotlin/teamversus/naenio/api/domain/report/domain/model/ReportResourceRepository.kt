package teamversus.naenio.api.domain.report.domain.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReportResourceRepository : ReactiveCrudRepository<Report, Long> {
}