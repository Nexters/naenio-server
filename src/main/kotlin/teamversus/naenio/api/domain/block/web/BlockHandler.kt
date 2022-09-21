package teamversus.naenio.api.domain.block.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.block.application.service.BlockService
import teamversus.naenio.api.filter.memberId

@Component
class BlockHandler(
    private val blockService: BlockService,
) {
    fun block(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(BlockRequest::class.java)
            .flatMap { blockService.block(request.memberId(), it.targetMemberId) }
            .flatMap { ServerResponse.noContent().build() }

    data class BlockRequest(
        val targetMemberId: Long,
    )
}




