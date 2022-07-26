package teamversus.naenio.api.domain.vote.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class VoteRouter(private val voteHandler: VoteHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/votes",
                method = [RequestMethod.POST],
                beanClass = VoteHandler::class,
                beanMethod = "cast",
                operation = Operation(
                    tags = ["투표"],
                    summary = "투표하기",
                    operationId = "castVote",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = VoteHandler.CastVoteRequest::class
                            )
                        )]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = VoteHandler.CastVoteResponse::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            )]
    )
    fun postRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/votes", voteHandler::cast)
        }
    }
}