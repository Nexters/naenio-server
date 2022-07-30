package teamversus.naenio.api.domain.commentlike.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
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
class CommentLikeRouter(private val commentLikeHandler: CommentLikeHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/comment-likes",
                method = [RequestMethod.POST],
                beanClass = CommentLikeHandler::class,
                beanMethod = "like",
                operation = Operation(
                    tags = ["댓글 좋아요"],
                    summary = "댓글 좋아요",
                    operationId = "createCommentLike",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = CommentLikeHandler.LikeCommentRequest::class
                            )
                        )]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = CommentLikeHandler.LikeCommentResponse::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/comment-likes",
                method = [RequestMethod.DELETE],
                beanClass = CommentLikeHandler::class,
                beanMethod = "unlike",
                operation = Operation(
                    tags = ["댓글 좋아요"],
                    summary = "댓글 좋아요 취소",
                    operationId = "deleteCommentLike",
                    parameters = [Parameter(
                        name = "id",
                        `in` = ParameterIn.PATH,
                        required = true
                    )],
                    responses = [
                        ApiResponse(
                            responseCode = "204",
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            )
        ]
    )
    fun commentLikeRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/comment-likes", commentLikeHandler::like)
            DELETE("/app/comment-likes/{id}", commentLikeHandler::unlike)
        }
    }
}