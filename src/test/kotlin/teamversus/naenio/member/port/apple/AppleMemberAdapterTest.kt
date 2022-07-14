package teamversus.naenio.member.port.apple

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import teamversus.naenio.api.member.port.member.adapter.AppleMemberAdapter

internal class AppleMemberAdapterTest {

    @Disabled
    @Test
    fun findBy() {
        StepVerifier.create(AppleMemberAdapter().findBy("token"))
            .assertNext { assertThat(it.authId).isNotNull() }
            .verifyComplete()
    }
}