package teamversus.naenio.api.core.post.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import teamversus.naenio.api.core.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.core.choice.application.ChoiceEditUseCase
import teamversus.naenio.api.core.post.application.PostCreateUseCase
import teamversus.naenio.api.core.post.application.PostEditUseCase
import teamversus.naenio.api.core.post.domain.model.PostRepository

@Service
class PostService(
    private val postRepository: PostRepository,
    private val choiceCreateUseCase: ChoiceCreateUseCase,
    private val choiceEditUseCase: ChoiceEditUseCase,
) : PostCreateUseCase, PostEditUseCase {

    @Transactional
    override fun create(command: PostCreateUseCase.Command, memberId: Long): Mono<PostCreateUseCase.Result> =
        postRepository.save(command.toPost(memberId))
            .flatMap { post ->
                choiceCreateUseCase.create(command.toChoiceCreateCommands(post.id))
                    .collectList()
                    .map { choices ->
                        PostCreateUseCase.Result(
                            post.id,
                            post.memberId,
                            post.title,
                            post.content,
                            choices.map { PostCreateUseCase.Result.Choice(it.id, it.postId, it.sequence, it.name) })
                    }
            }

    @Transactional
    override fun edit(id: Long, command: PostEditUseCase.Command, memberId: Long): Mono<PostEditUseCase.Result> =
        Mono.zip(
            postRepository.findById(id)
                .switchIfEmpty(Mono.error(IllegalArgumentException("존재하지 않는 게시글 postId=${id}")))
                .flatMap {
                    if (it.memberId != memberId) {
                        return@flatMap Mono.error(IllegalArgumentException("회원 아이디가 일치하지 않습니다. postMemberId=${it.memberId}, requestMemberId=${memberId}"))
                    }
                    postRepository.save(command.toDomain(it.id, memberId))
                },
            choiceEditUseCase.edit(command.toChoiceEditCommands(id))
                .collectList()
        )
            .map { tuple ->
                PostEditUseCase.Result(
                    tuple.t1.id,
                    tuple.t1.memberId,
                    tuple.t1.title,
                    tuple.t1.content,
                    tuple.t2.map { PostEditUseCase.Result.Choice(it.id, it.sequence, it.name) })
            }
}