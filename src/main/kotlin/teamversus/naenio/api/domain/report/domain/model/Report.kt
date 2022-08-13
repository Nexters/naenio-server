package teamversus.naenio.api.domain.report.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Report(
    @Id
    val id: Long = 0,
    val memberId: Long,
    val targetMemberId: Long,
    val resource: ReportResource,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Report = Report(id, memberId, targetMemberId, resource)
}