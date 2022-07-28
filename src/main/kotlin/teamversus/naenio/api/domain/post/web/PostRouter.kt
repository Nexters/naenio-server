package teamversus.naenio.api.domain.post.web

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
class PostRouter(private val postHandler: PostHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/posts",
                method = [RequestMethod.POST],
                beanClass = PostHandler::class,
                beanMethod = "create",
                operation = Operation(
                    tags = ["게시글"],
                    summary = "게시글 작성",
                    operationId = "createPost",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = PostHandler.CreatePostRequest::class
                            )
                        )]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = PostHandler.CreatePostResponse::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/posts/{id}",
                method = [RequestMethod.DELETE],
                beanClass = PostHandler::class,
                beanMethod = "delete",
                operation = Operation(
                    tags = ["게시글"],
                    summary = "게시글 삭제",
                    operationId = "deletePost",
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
        ],
    )
    fun postRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/posts", postHandler::create)
            DELETE("/app/posts/{id}", postHandler::delete)
        }
    }
}