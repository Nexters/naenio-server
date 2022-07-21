package teamversus.naenio.api.query

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.category.domain.model.CategoryRepository
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.query.result.WebPostDetailQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class WebPostFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
    private val categoryRepository: CategoryRepository,
) {
    fun findDetailById(request: ServerRequest): Mono<ServerResponse> {
        val postId = request.pathVariable("id").toLong()
        return Mono.zip(postRepository.findById(postId), choiceRepository.findAllByPostId(postId).collectList())
            .flatMap {
                Mono.zip(Mono.just(it.t1), Mono.just(it.t2), categoryRepository.findById(it.t1.categoryId))
                    .map { tuple ->
                        WebPostDetailQueryResult(
                            tuple.t1.id,
                            tuple.t1.memberId,
                            tuple.t1.title,
                            tuple.t1.content,
                            WebPostDetailQueryResult.CategoryResult(tuple.t3.id, tuple.t3.name),
                            tuple.t2.map { WebPostDetailQueryResult.ChoiceResult(it.id, it.sequence, it.name) },
                            tuple.t1.createdDateTime,
                            tuple.t1.lastModifiedDateTime
                        )
                    }
                    .flatMap(::okWithBody)
            }
    }
}
