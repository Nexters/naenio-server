package teamversus.naenio.api.domain.post.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.domain.choice.application.ChoiceDeleteUseCase
import teamversus.naenio.api.domain.post.application.PostCreateUseCase
import teamversus.naenio.api.domain.post.application.PostDeleteUseCase
import teamversus.naenio.api.domain.post.domain.model.PostRepository

@Service
class PostService(
    private val postRepository: PostRepository,
    private val choiceCreateUseCase: ChoiceCreateUseCase,
    private val choiceDeleteUseCase: ChoiceDeleteUseCase,
) : PostCreateUseCase, PostDeleteUseCase {

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
                            choices.map { PostCreateUseCase.Result.Choice(it.id, it.postId, it.sequence, it.name) },
                            post.createdDateTime,
                            post.lastModifiedDateTime
                        )
                    }
            }

    @Transactional
    override fun deleteById(id: Long): Mono<Void> =
        Mono.just(id)
            .filterWhen { postRepository.existsById(it) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("존재하지 않는 게시글 입니다. id=${id}}")) }
            .flatMap { choiceDeleteUseCase.deleteAllByPostId(id) }
            .flatMap { postRepository.deleteById(id) }
}