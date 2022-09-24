package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.block.domain.model.BlockRepository
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import teamversus.naenio.api.domain.comment.domain.model.CommentRepository
import teamversus.naenio.api.domain.commentlike.domain.model.CommentLikeRepository
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.query.result.AppCommentRepliesQueryResult
import teamversus.naenio.api.query.result.AppPostCommentsOfMeQueryResult
import teamversus.naenio.api.query.result.AppPostCommentsQueryResult
import teamversus.naenio.api.support.lastCommentIdInQueryParam
import teamversus.naenio.api.support.okWithBody
import teamversus.naenio.api.support.pageableOfSizeInQueryParam
import teamversus.naenio.api.support.pathVariableId

@Component
class AppCommentFetcher(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val blockRepository: BlockRepository,
) {
    fun findPostComments(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request.pathVariableId())
            .filterWhen { postRepository.existsById(it) }
            .switchIfEmpty(Mono.error(IllegalArgumentException("삭제된 게시글 입니다.")))
            .flatMap { blockRepository.findAllByMemberId(request.memberId()).map { it.targetMemberId }.collectList() }
            .flatMap {
                Mono.zip(
                    commentRepository.countByParentIdAndParentTypeAndMemberIdNotIn(
                        request.pathVariableId(),
                        CommentParent.POST,
                        it
                    ),
                    commentRepository.findAllByIdLessThanAndMemberIdNotInAndParentIdAndParentTypeOrderByIdDesc(
                        request.lastCommentIdInQueryParam(),
                        it,
                        request.pathVariableId(),
                        CommentParent.POST,
                        request.pageableOfSizeInQueryParam()
                    )
                        .flatMap { comment ->
                            Mono.zip(
                                memberRepository.findById(comment.memberId),
                                commentLikeRepository.countByCommentId(comment.id),
                                commentLikeRepository.existsByMemberIdAndCommentId(request.memberId(), comment.id),
                                commentRepository.countByParentIdAndParentTypeAndMemberIdNotIn(
                                    comment.id,
                                    CommentParent.COMMENT,
                                    it
                                )
                            )
                                .map { tuple ->
                                    AppPostCommentsQueryResult.AppPostComment(
                                        comment.id,
                                        AppPostCommentsQueryResult.AppPostComment.AppPostCommentAuthor(
                                            tuple.t1.id,
                                            tuple.t1.nickname,
                                            tuple.t1.profileImageIndex
                                        ),
                                        comment.content,
                                        comment.createdDateTime,
                                        tuple.t2,
                                        tuple.t3,
                                        tuple.t4
                                    )
                                }
                        }
                        .collectList()
                )
            }
            .map { AppPostCommentsQueryResult(it.t1, it.t2) }
            .flatMap { okWithBody(it) }

    fun findCommentReplies(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request.pathVariableId())
            .filterWhen { commentRepository.existsById(it) }
            .switchIfEmpty(Mono.error(IllegalArgumentException("삭제된 댓글 입니다.")))
            .flatMap { blockRepository.findAllByMemberId(request.memberId()).map { it.targetMemberId }.collectList() }
            .flatMap {
                commentRepository.findAllByIdLessThanAndMemberIdNotInAndParentIdAndParentTypeOrderByIdDesc(
                    request.lastCommentIdInQueryParam(),
                    it,
                    request.pathVariableId(),
                    CommentParent.COMMENT,
                    request.pageableOfSizeInQueryParam()
                )
                    .flatMap { comment ->
                        Mono.zip(
                            memberRepository.findById(comment.memberId),
                            commentLikeRepository.countByCommentId(comment.id),
                            commentLikeRepository.existsByMemberIdAndCommentId(request.memberId(), comment.id),
                        )
                            .map { tuple ->
                                AppCommentRepliesQueryResult.CommentReply(
                                    comment.id,
                                    AppCommentRepliesQueryResult.CommentReply.CommentReplyAuthor(
                                        tuple.t1.id,
                                        tuple.t1.nickname,
                                        tuple.t1.profileImageIndex
                                    ),
                                    comment.content,
                                    comment.createdDateTime,
                                    tuple.t2,
                                    tuple.t3
                                )
                            }
                    }
                    .collectList()
            }
            .map { AppCommentRepliesQueryResult(it) }
            .flatMap { okWithBody(it) }


    fun findAllByMe(request: ServerRequest): Mono<ServerResponse> =
        commentRepository.findAllByIdLessThanAndMemberIdAndParentTypeOrderByIdDesc(
            request.lastCommentIdInQueryParam(),
            request.memberId(),
            CommentParent.POST,
            request.pageableOfSizeInQueryParam()
        )
            .flatMap { comment ->
                blockRepository.findAllByMemberId(request.memberId()).map { it.targetMemberId }.collectList()
                    .flatMap { postRepository.findByIdAndMemberIdNotIn(comment.parentId, it) }
                    .flatMap { post ->
                        memberRepository.findById(post.memberId)
                            .map { author ->
                                AppPostCommentsOfMeQueryResult.AppPostCommentsOfMe(
                                    comment.id,
                                    comment.content,
                                    AppPostCommentsOfMeQueryResult.AppPostCommentsOfMe.AppPostCommentsOfMePost(
                                        post.id,
                                        AppPostCommentsOfMeQueryResult.AppPostCommentsOfMe.AppPostCommentsOfMePost.AppPostCommentsOfMePostAuthor(
                                            author.id,
                                            author.nickname,
                                            author.profileImageIndex
                                        ),
                                        post.title
                                    )
                                )
                            }
                    }
            }
            .collectList()
            .map { AppPostCommentsOfMeQueryResult(it.sortedByDescending { comment -> comment.id }) }
            .flatMap { okWithBody(it) }
}
