package teamversus.naenio.api.command.member.application

import reactor.core.publisher.Mono

interface MemberExistByNicknameUseCase {
    fun exist(nickname: String): Mono<Boolean>
}
