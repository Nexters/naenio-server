package teamversus.naenio.api.domain.member.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Member(
    @Id
    val id: Long = 0,
    val authId: String,
    val authServiceType: AuthServiceType,
    var nickname: String?,
    var profileImageIndex: Int?,
    var withdraw: Boolean,
    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.MIN,
    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime = LocalDateTime.MIN,
) {
    fun withId(id: Long): Member = Member(id, authId, authServiceType, nickname, profileImageIndex, withdraw)

    fun changeNickname(nickname: String): Member {
        this.nickname = nickname
        return this
    }

    fun changeProfileImage(profileImageIndex: Int): Member {
        this.profileImageIndex = profileImageIndex
        return this
    }

    fun withdraw(): Member {
        this.withdraw = true
        this.nickname = null
        this.profileImageIndex = null
        return this
    }
}