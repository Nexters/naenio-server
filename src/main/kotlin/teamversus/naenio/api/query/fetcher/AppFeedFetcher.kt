package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import teamversus.naenio.api.domain.block.domain.model.BlockRepository
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.domain.member.domain.model.Member
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.post.domain.model.Post
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.query.model.PostCommentCount
import teamversus.naenio.api.query.model.PostCommentCountRepository
import teamversus.naenio.api.query.model.SortType
import teamversus.naenio.api.query.result.AppPostDetailQueryResult
import teamversus.naenio.api.query.result.AppPostsQueryResult
import teamversus.naenio.api.support.lastPostIdInQueryParam
import teamversus.naenio.api.support.okWithBody
import teamversus.naenio.api.support.pageableOfSizeInQueryParam
import java.time.LocalDateTime

@Component
class AppFeedFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
    private val memberRepository: MemberRepository,
    private val voteRepository: VoteRepository,
    private val postCommentCountRepository: PostCommentCountRepository,
    private val blockRepository: BlockRepository,
) {
    fun findFeed(request: ServerRequest): Mono<ServerResponse> =
        findPosts(request)
            .flatMap { post ->
                Mono.zip(
                    choiceRepository.findAllByPostId(post.id)
                        .flatMap {
                            Mono.zip(
                                voteRepository.existsByChoiceIdAndMemberId(it.id, request.memberId()),
                                voteRepository.countByChoiceId(it.id)
                            )
                                .map { tuple ->
                                    AppPostDetailQueryResult.AppPostDetailChoice(
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
                    postCommentCountRepository.findByPostId(post.id)
                        .switchIfEmpty(Mono.just(PostCommentCount(0, post.id, 0))),
                )
                    .map {
                        AppPostDetailQueryResult(
                            post.id,
                            AppPostDetailQueryResult.AppPostDetailAuthor(
                                it.t2.id,
                                it.t2.nickname,
                                it.t2.profileImageIndex
                            ),
                            post.title,
                            post.content,
                            it.t1.sortedBy { choice -> choice.sequence },
                            it.t3.commentCount.toLong()
                        )
                    }
            }
            .collectList()
            .map { AppPostsQueryResult(it) }
            .flatMap { okWithBody(it) }

    private fun findPosts(request: ServerRequest): Flux<Post> {
        val sortType = request.queryParam("sortType")

        if (sortType.isEmpty) {
            return blockRepository.findAllByMemberId(request.memberId()).map { it.targetMemberId }.collectList()
                .toFlux()
                .flatMap {
                    postRepository.findAllByIdLessThanAndMemberIdNotInOrderByIdDesc(
                        request.lastPostIdInQueryParam(),
                        it,
                        request.pageableOfSizeInQueryParam()
                    )
                }
        }
        if (sortType.get() == SortType.MY_POST.name) {
            return postRepository.findAllByMemberIdAndIdLessThanOrderByIdDesc(
                request.memberId(),
                request.lastPostIdInQueryParam(),
                request.pageableOfSizeInQueryParam()
            )
        }
        if (sortType.get() == SortType.VOTED_BY_ME.name) {
            if (request.lastPostIdInQueryParam() == Long.MAX_VALUE) {
                return voteRepository.findByMemberIdAndLastModifiedDateTimeBeforeOrderByLastModifiedDateTimeDesc(
                    request.memberId(),
                    LocalDateTime.now(),
                    request.pageableOfSizeInQueryParam()
                )
                    .map { it.postId }
                    .collectList()
                    .flatMap { ids ->
                        postRepository.findAllById(ids)
                            .collectMap { post -> post.id }
                            .map { posts -> ids.mapNotNull { posts[it] } }
                    }
                    .flatMapIterable { it }
            }

            return voteRepository.findByPostIdAndMemberId(request.lastPostIdInQueryParam(), request.memberId())
                .map { it.lastModifiedDateTime }
                .flatMap { lastModifiedDateTime ->
                    voteRepository.findByMemberIdAndLastModifiedDateTimeBeforeOrderByLastModifiedDateTimeDesc(
                        request.memberId(),
                        lastModifiedDateTime,
                        request.pageableOfSizeInQueryParam()
                    )
                        .map { it.postId }
                        .collectList()
                        .flatMap { ids ->
                            postRepository.findAllById(ids)
                                .collectMap { post -> post.id }
                                .map { posts -> ids.mapNotNull { posts[it] } }
                        }
                }.flatMapIterable { it }
        }
        throw IllegalArgumentException("지원하지 않는 sortType. sortType=${sortType}")
    }
}