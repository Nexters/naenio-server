package teamversus.naenio.api.query.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface NoticeRepository : ReactiveCrudRepository<Notice, Long> {
}