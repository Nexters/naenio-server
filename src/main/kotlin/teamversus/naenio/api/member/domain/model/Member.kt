package teamversus.naenio.api.member.domain.model

import org.springframework.data.annotation.Id

class Member(
    @Id
    val id: Long = 0,
    val authId: String,
    val authServiceType: AuthServiceType,
    var nickname: String?,
) {
    fun withId(id: Long): Member = Member(id, authId, authServiceType, nickname)

    fun changeNickname(nickname: String): Member {
        this.nickname = nickname
        return this
    }
}