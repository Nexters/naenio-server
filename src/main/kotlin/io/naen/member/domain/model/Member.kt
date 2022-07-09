package io.naen.member.domain.model

import org.springframework.data.annotation.Id

class Member(
    @Id
    val id: Long = 0,
    val oauthId: String,
    val nickname: String,
) {
    fun withId(id: Long): Member = Member(id, oauthId, nickname)
}