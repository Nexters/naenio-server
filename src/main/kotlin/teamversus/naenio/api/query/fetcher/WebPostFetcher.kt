package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.query.model.PostCommentCount
import teamversus.naenio.api.query.model.PostCommentCountRepository
import teamversus.naenio.api.query.model.PostVoteCount
import teamversus.naenio.api.query.model.PostVoteCountRepository
import teamversus.naenio.api.query.result.WebPostDetailQueryResult
import teamversus.naenio.api.support.okWithBody
import teamversus.naenio.api.support.pathVariableId

@Component
class WebPostFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
    private val memberRepository: MemberRepository,
    private val postCommentCountRepository: PostCommentCountRepository,
    private val postVoteCountRepository: PostVoteCountRepository,
) {
    fun findDetailById(request: ServerRequest): Mono<ServerResponse> {
        val postId = request.pathVariableId()
        return Mono.zip(postRepository.findById(postId), choiceRepository.findAllByPostId(postId).collectList())
            .flatMap {
                Mono.zip(
                    Mono.just(it.t1),
                    Mono.just(it.t2),
                    memberRepository.findById(it.t1.memberId),
                    postCommentCountRepository.findByPostId(it.t1.id)
                        .switchIfEmpty(Mono.just(PostCommentCount(0, it.t1.id, 0))),
                    postVoteCountRepository.findByPostId(it.t1.id)
                        .switchIfEmpty(Mono.just(PostVoteCount(0, it.t1.id, 0)))
                )
                    .map { tuple ->
                        WebPostDetailQueryResult(
                            tuple.t1.id,
                            WebPostDetailQueryResult.WebPostDetailAuthor(
                                tuple.t3.id,
                                tuple.t3.nickname,
                                tuple.t3.profileImageIndex
                            ),
                            tuple.t1.title,
                            tuple.t1.content,
                            tuple.t2.map { choice ->
                                WebPostDetailQueryResult.WebPostDetailChoice(
                                    choice.id,
                                    choice.sequence,
                                    choice.name
                                )
                            }.sortedBy { choice -> choice.sequence },
                            tuple.t4.commentCount.toLong(),
                            tuple.t5.count,
                        )
                    }
                    .flatMap(::okWithBody)
            }
    }
}
