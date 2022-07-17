package teamversus.naenio.api.support

import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse


fun okWithBody(it: Any) = ServerResponse.ok().body(BodyInserters.fromValue(it))