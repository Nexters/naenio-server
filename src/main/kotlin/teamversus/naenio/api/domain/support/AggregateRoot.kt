package teamversus.naenio.api.domain.support

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.LocalDateTime

abstract class AggregateRoot<A : AbstractAggregateRoot<A>> : AbstractAggregateRoot<A>() {
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN
}