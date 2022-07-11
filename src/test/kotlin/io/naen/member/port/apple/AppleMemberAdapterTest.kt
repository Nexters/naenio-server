package io.naen.member.port.apple

import io.naen.member.port.member.adapter.AppleMemberAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class AppleMemberAdapterTest {

    @Disabled
    @Test
    fun findBy() {
        StepVerifier.create(AppleMemberAdapter().findBy("token"))
            .assertNext { assertThat(it.authId).isNotNull() }
            .verifyComplete()
    }
}