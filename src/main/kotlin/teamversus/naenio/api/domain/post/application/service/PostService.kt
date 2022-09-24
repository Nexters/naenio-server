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
    override fun create(command: PostCreateUseCase.Command, memberId: Long): Mono<PostCreateUseCase.Result> {
        require(command.title.length <= 70) { "제목은 최대 70자 입니다." }
        require(command.content.length <= 99) { "내용은 최대 99자 입니다." }

        return postRepository.save(command.toPost(memberId))
            .flatMap { post ->
                choiceCreateUseCase.create(command.toChoiceCreateCommands(post.id))
                    .collectList()
                    .map { choices ->
                        PostCreateUseCase.Result(
                            post.id,
                            post.memberId,
                            post.title,
                            post.content,
                            choices.map {
                                PostCreateUseCase.Result.Choice(
                                    it.id,
                                    it.postId,
                                    it.sequence,
                                    it.name
                                )
                            },
                            post.createdDateTime,
                            post.lastModifiedDateTime
                        )
                    }
            }
    }


    @Transactional
    override fun deleteById(id: Long, memberId: Long): Mono<Void> =
        Mono.just(id)
            .filterWhen { postRepository.existsByIdAndMemberId(it, memberId) }
            .switchIfEmpty { Mono.error(IllegalArgumentException("이미 삭제된 게시글 입니다.")) }
            .flatMap {
                Mono.zip(choiceDeleteUseCase.deleteAllByPostId(id), postRepository.deleteById(id))
                    .flatMap { Mono.empty() }
            }

}
