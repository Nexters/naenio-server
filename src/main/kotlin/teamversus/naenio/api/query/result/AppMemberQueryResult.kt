package teamversus.naenio.api.query.result

import teamversus.naenio.api.domain.member.domain.model.AuthServiceType

data class AppMemberQueryResult(
    val id: Long,
    val authServiceType: AuthServiceType,
    val nickname: String?,
    val profileImageIndex: Int?,
)
