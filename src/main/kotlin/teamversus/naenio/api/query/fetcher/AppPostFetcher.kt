package teamversus.naenio.api.query.fetcher

import org.springframework.data.domain.Pageable
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
import teamversus.naenio.api.domain.post.domain.model.Post
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.query.model.*
import teamversus.naenio.api.query.result.AppPostDetailQueryResult
import teamversus.naenio.api.query.result.AppPostsQueryResult
import teamversus.naenio.api.support.okWithBody
import teamversus.naenio.api.support.pathVariableId
import java.time.LocalDate
import kotlin.random.Random

@Component
class AppPostFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
    private val memberRepository: MemberRepository,
    private val commentRepository: CommentRepository,
    private val voteRepository: VoteRepository,
    private val postVoteByDayRepository: PostVoteByDayRepository,
    private val postVoteCountRepository: PostVoteCountRepository,
    private val postChoiceVoteCountRepository: PostChoiceVoteCountRepository,
    private val postCommentCountRepository: PostCommentCountRepository,
) {
    fun findAllByTheme(request: ServerRequest): Mono<ServerResponse> {
        val theme = request.queryParam("theme")
            .map { Theme.valueOf(it) }
            .orElseThrow { IllegalArgumentException("요청과 일치하는 테마가 존재하지 않습니다.") }

        if (theme == Theme.TODAY_VOTE) {
            return postVoteByDayRepository.findAllByAggregateDateOrderByCountDesc(LocalDate.now(), Pageable.ofSize(10))
                .switchIfEmpty(
                    postVoteByDayRepository.findAllByAggregateDateOrderByCountDesc(
                        LocalDate.now().minusDays(1L), Pageable.ofSize(10)
                    )
                )
                .flatMap { postRepository.findById(it.postId) }
                .flatMap { fetchWith(it, request.memberId()) }
                .collectList()
                .map { AppPostsQueryResult(it) }
                .flatMap { okWithBody(it) }
        }
        if (theme == Theme.HALL_OF_FAME) {
            return postVoteCountRepository.findAllByOrderByCountDesc(Pageable.ofSize(10))
                .flatMap { postRepository.findById(it.postId) }
                .flatMap { fetchWith(it, request.memberId()) }
                .collectList()
                .map { AppPostsQueryResult(it) }
                .flatMap { okWithBody(it) }
        }
        if (theme == Theme.GOLD_BALANCE) {
            return postChoiceVoteCountRepository.findAllByTotalCountGreaterThanEqualOrderByRateDesc(
                10,
                Pageable.ofSize(10)
            )
                .flatMap { postRepository.findById(it.postId) }
                .flatMap { fetchWith(it, request.memberId()) }
                .collectList()
                .map { AppPostsQueryResult(it) }
                .flatMap { okWithBody(it) }
        }
        if (theme == Theme.COLLAPSED_BALANCE) {
            return postChoiceVoteCountRepository.findAllByTotalCountGreaterThanEqualOrderByRateAsc(
                10,
                Pageable.ofSize(10)
            )
                .flatMap { postRepository.findById(it.postId) }
                .flatMap { fetchWith(it, request.memberId()) }
                .collectList()
                .map { AppPostsQueryResult(it) }
                .flatMap { okWithBody(it) }
        }
        if (theme == Theme.NOISY) {
            return postCommentCountRepository.findAllByOrderByCommentCountDesc(Pageable.ofSize(10))
                .flatMap { postRepository.findById(it.postId) }
                .flatMap { fetchWith(it, request.memberId()) }
                .collectList()
                .map { AppPostsQueryResult(it) }
                .flatMap { okWithBody(it) }
        }
        throw IllegalArgumentException("요청과 일치하는 테마가 존재하지 않습니다.")
    }

    fun findDetailById(request: ServerRequest): Mono<ServerResponse> =
        postRepository.findById(request.pathVariableId())
            .flatMap { fetchWith(it, request.memberId()) }
            .flatMap { okWithBody(it) }


    fun findDetailByRandom(request: ServerRequest): Mono<ServerResponse> =
        postRepository.count()
            .flatMap { postRepository.findById(Random.nextLong(1, it + 1)) }
            .repeatWhenEmpty(10) {
                postRepository.count()
                    .flatMap { postRepository.findById(Random.nextLong(1, it + 1)) }
            }
            .flatMap { fetchWith(it, request.memberId()) }
            .flatMap { okWithBody(it) }

    private fun fetchWith(post: Post, memberId: Long) =
        Mono.zip(
            choiceRepository.findAllByPostId(post.id)
                .flatMap {
                    Mono.zip(
                        voteRepository.existsByChoiceIdAndMemberId(it.id, memberId),
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
            commentRepository.countByParentIdAndParentType(post.id, CommentParent.POST)
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
                    it.t1,
                    it.t3
                )
            }
}
