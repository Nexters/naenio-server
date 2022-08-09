package teamversus.naenio.api.domain.vote.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.vote.application.VoteCastUseCase
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.support.okWithBody
import java.time.LocalDateTime

@Component
class VoteHandler(
    private val voteCastUseCase: VoteCastUseCase,
) {
    fun cast(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(CastVoteRequest::class.java)
            .flatMap { voteCastUseCase.cast(it.toCommand(), request.memberId()) }
            .map { CastVoteResponse.from(it) }
            .flatMap(::okWithBody)

    data class CastVoteRequest(
        val postId: Long,
        val choiceId: Long,
    ) {
        fun toCommand(): VoteCastUseCase.Command = VoteCastUseCase.Command(postId, choiceId)
    }

    data class CastVoteResponse(
        val id: Long,
        val postId: Long,
        val choiceId: Long,
        val memberId: Long,
        val createdDateTime: LocalDateTime,
        val lastModifiedDateTime: LocalDateTime,
    ) {
        companion object {
            fun from(result: VoteCastUseCase.Result) =
                CastVoteResponse(
                    result.id,
                    result.postId,
                    result.choiceId,
                    result.memberId,
                    result.createdDateTime,
                    result.lastModifiedDateTime
                )
        }
    }
}




