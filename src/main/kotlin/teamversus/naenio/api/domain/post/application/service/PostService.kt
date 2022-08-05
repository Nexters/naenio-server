package teamversus.naenio.api.domain.post.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import teamversus.naenio.api.domain.category.application.CategoryExistUseCase
import teamversus.naenio.api.domain.category.application.CategoryLoadUseCase
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
    private val categoryExistUseCase: CategoryExistUseCase,
    private val categoryLoadUseCase: CategoryLoadUseCase,
) : PostCreateUseCase, PostDeleteUseCase {

    @Transactional
    override fun create(command: PostCreateUseCase.Command, memberId: Long): Mono<PostCreateUseCase.Result> =
        Mono.just(command)
            .filterWhen { categoryExistUseCase.existById(it.categoryId) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("카테고리가 존재하지 않습니다. categoryId=${command.categoryId}")) }
            .flatMap {
                Mono.zip(
                    postRepository.save(command.toPost(memberId)),
                    categoryLoadUseCase.findById(it.categoryId)
                )
            }
            .flatMap { tuple ->
                choiceCreateUseCase.create(command.toChoiceCreateCommands(tuple.t1.id))
                    .collectList()
                    .map { choices ->
                        PostCreateUseCase.Result(
                            tuple.t1.id,
                            tuple.t1.memberId,
                            tuple.t1.title,
                            tuple.t1.content,
                            PostCreateUseCase.Result.Category(tuple.t2.id, tuple.t2.name),
                            choices.map { PostCreateUseCase.Result.Choice(it.id, it.postId, it.sequence, it.name) },
                            tuple.t1.createdDateTime,
                            tuple.t1.lastModifiedDateTime
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