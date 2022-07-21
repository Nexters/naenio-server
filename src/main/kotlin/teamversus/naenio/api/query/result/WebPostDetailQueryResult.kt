package teamversus.naenio.api.query.result

import java.time.LocalDateTime

data class WebPostDetailQueryResult(
    val id: Long,
    val memberId: Long,
    val title: String,
    val content: String,
    val category: CategoryResult,
    val choices: List<ChoiceResult>,
    val createdDateTime: LocalDateTime,
    val lastModifiedDateTime: LocalDateTime,
) {
    data class ChoiceResult(
        val id: Long,
        val sequence: Int,
        val name: String,
    )

    data class CategoryResult(
        val id: Long,
        val name: String,
    )
}
