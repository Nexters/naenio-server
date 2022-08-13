package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import teamversus.naenio.api.domain.comment.domain.model.CommentRepository
import teamversus.naenio.api.domain.member.domain.model.Member
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.query.result.AppPostDetailQueryResult
import teamversus.naenio.api.support.okWithBody
import teamversus.naenio.api.support.pathVariableId

@Component
class AppPostFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
    private val memberRepository: MemberRepository,
    private val commentRepository: CommentRepository,
    private val voteRepository: VoteRepository,
) {
    fun findDetailById(request: ServerRequest): Mono<ServerResponse> =
        postRepository.findById(request.pathVariableId())
            .flatMap { post ->
                Mono.zip(
                    choiceRepository.findAllByPostId(post.id)
                        .flatMap {
                            Mono.zip(
                                voteRepository.existsByChoiceIdAndMemberId(it.id, request.memberId()),
                                voteRepository.countByChoiceId(it.id)
                            )
                                .map { tuple ->
                                    AppPostDetailQueryResult.Choice(
                                        it.id,
                                        it.sequence,
                                        it.name,
                                        tuple.t1,
                                        tuple.t2
                                    )
                                }
                        }
                        .collectList(),
                    memberRepository.findById(post.memberId)
                        .switchIfEmpty { Mono.just(Member.withdrawMember()) },
                    commentRepository.countByParentIdAndParentType(post.id, CommentParent.POST)
                )
                    .map {
                        AppPostDetailQueryResult(
                            post.id,
                            AppPostDetailQueryResult.Author(
                                it.t2.id,
                                it.t2.nickname,
                                it.t2.profileImageIndex
                            ),
                            post.title,
                            post.content,
                            it.t1,
                            it.t3
                        )
                    }
            }
            .flatMap { okWithBody(it) }
}
