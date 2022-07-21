package teamversus.naenio.api.post.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import teamversus.naenio.api.choice.application.ChoiceCreateUseCase
import teamversus.naenio.api.post.application.PostCreateUseCase
import teamversus.naenio.api.post.domain.model.PostRepository

@Service
class PostService(
    private val postRepository: PostRepository,
    private val choiceCreateUseCase: ChoiceCreateUseCase,
) : PostCreateUseCase {

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
}