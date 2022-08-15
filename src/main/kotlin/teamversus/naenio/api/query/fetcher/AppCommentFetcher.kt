package teamversus.naenio.api.query.fetcher

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.comment.domain.model.CommentParent
import teamversus.naenio.api.domain.comment.domain.model.CommentRepository
import teamversus.naenio.api.domain.commentlike.domain.model.CommentLikeRepository
import teamversus.naenio.api.domain.member.domain.model.MemberRepository
import teamversus.naenio.api.domain.post.domain.model.PostRepository
import teamversus.naenio.api.filter.memberId
import teamversus.naenio.api.query.result.AppCommentRepliesQueryResult
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
) {
    fun findPostComments(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request.pathVariableId())
            .filterWhen { postRepository.existsById(it) }
            .switchIfEmpty(Mono.error(IllegalArgumentException("게시글이 존재하지 않습니다. postId=${request.pathVariableId()}")))
            .flatMap {
                Mono.zip(
                    commentRepository.countByParentIdAndParentType(it, CommentParent.POST),
                    commentRepository.findAllByIdLessThanAndParentIdAndParentTypeOrderByIdDesc(
                        request.lastCommentIdInQueryParam(),
                        it,
                        CommentParent.POST,
                        request.pageableOfSizeInQueryParam()
                    )
                        .flatMap { comment ->
                            Mono.zip(
                                memberRepository.findById(comment.memberId),
                                commentLikeRepository.countByCommentId(comment.id),
                                commentLikeRepository.existsByMemberIdAndCommentId(request.memberId(), comment.id),
                                commentRepository.countByParentIdAndParentType(comment.id, CommentParent.COMMENT)
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
            .switchIfEmpty(Mono.error(IllegalArgumentException("댓글이 존재하지 않습니다. commentId=${request.pathVariableId()}")))
            .flatMap {
                commentRepository.findAllByIdLessThanAndParentIdAndParentTypeOrderByIdDesc(
                    request.lastCommentIdInQueryParam(),
                    it,
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


}
