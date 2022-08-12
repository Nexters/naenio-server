package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.category.domain.model.CategoryRepository
import teamversus.naenio.api.domain.choice.domain.model.ChoiceRepository
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import teamversus.naenio.api.domain.comment.domain.model.CommentRepository
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.domain.vote.domain.model.VoteRepository
import teamversus.naenio.api.query.result.WebPostDetailQueryResult
import teamversus.naenio.api.support.okWithBody

@Component
class WebPostFetcher(
    private val postRepository: PostRepository,
    private val choiceRepository: ChoiceRepository,
    private val categoryRepository: CategoryRepository,
    private val memberRepository: MemberRepository,
    private val commentRepository: CommentRepository,
    private val voteRepository: VoteRepository,
) {
    fun findDetailById(request: ServerRequest): Mono<ServerResponse> {
        val postId = request.pathVariable("id").toLong()
        return Mono.zip(postRepository.findById(postId), choiceRepository.findAllByPostId(postId).collectList())
            .flatMap {
                Mono.zip(
                    Mono.just(it.t1),
                    Mono.just(it.t2),
                    categoryRepository.findById(it.t1.categoryId),
                    memberRepository.findById(it.t1.memberId),
                    commentRepository.countByParentIdAndParentType(it.t1.id, CommentParent.POST),
                    voteRepository.countByChoiceIdIn(it.t2.map { choice -> choice.id })
                )
                    .map { tuple ->
                        WebPostDetailQueryResult(
                            tuple.t1.id,
                            WebPostDetailQueryResult.Author(tuple.t4.id, tuple.t4.nickname, tuple.t4.profileImageIndex),
                            tuple.t1.title,
                            tuple.t1.content,
                            tuple.t2.map { choice ->
                                WebPostDetailQueryResult.Choice(
                                    choice.id,
                                    choice.sequence,
                                    choice.name
                                )
                            },
                            tuple.t5,
                            tuple.t6,
                        )
                    }
                    .flatMap(::okWithBody)
            }
    }
}
