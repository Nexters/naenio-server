package com.example.priorlearning.web

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Component
class WebRouter {
    fun routerFunction(): RouterFunction<ServerResponse> = router {
        GET("/") { ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index") }
    }
}