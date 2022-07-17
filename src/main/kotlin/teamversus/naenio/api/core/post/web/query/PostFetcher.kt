package teamversus.naenio.api.core.post.web.query

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.core.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.core.post.domain.model.PostRepository
import teamversus.naenio.api.support.okWithBody

@Component
class PostFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
) {
    fun findDetailByIdForWeb(request: ServerRequest): Mono<ServerResponse> {
        val postId = request.pathVariable("id").toLong()
        return Mono.zip(postRepository.findById(postId), choiceRepository.findAllByPostId(postId).collectList())
            .map { tuple ->
                WebPostDetailQueryResult(
                    tuple.t1.id,
                    tuple.t1.memberId,
                    tuple.t1.title,
                    tuple.t1.content,
                    tuple.t2.map { WebPostDetailQueryResult.Choice(it.id, it.sequence, it.name) }
                )
            }
            .flatMap(::okWithBody)
    }
}
