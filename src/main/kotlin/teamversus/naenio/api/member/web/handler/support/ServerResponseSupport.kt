package teamversus.naenio.api.member.web.handler.support

import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse


fun okWithBody(it: Any) = ServerResponse.ok().body(BodyInserters.fromValue(it))