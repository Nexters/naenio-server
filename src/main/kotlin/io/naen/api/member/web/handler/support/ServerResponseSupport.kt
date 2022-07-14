package io.naen.api.member.web.handler.support

import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse


fun okWithBody(it: Any) = ServerResponse.ok().body(BodyInserters.fromValue(Response(it)))

data class Response(
    val `data`: Any? = null,
    val message: String? = null,
)