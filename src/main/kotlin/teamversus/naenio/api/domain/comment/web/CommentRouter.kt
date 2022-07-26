package teamversus.naenio.api.domain.comment.web

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
class CommentRouter(private val commentHandler: CommentHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/comments",
                method = [RequestMethod.POST],
                beanClass = CommentHandler::class,
                beanMethod = "create",
                operation = Operation(
                    tags = ["댓글"],
                    summary = "댓글 생성",
                    operationId = "createComment",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = CommentHandler.CreateCommentRequest::class
                            )
                        )]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = CommentHandler.CreateCommentResponse::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            )]
    )
    fun commentRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/votes", commentHandler::create)
        }
    }
}