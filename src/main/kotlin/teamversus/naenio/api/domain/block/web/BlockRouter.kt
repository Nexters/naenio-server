package teamversus.naenio.api.domain.block.web

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
class BlockRouter(private val blockHandler: BlockHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/blocks",
                method = [RequestMethod.POST],
                beanClass = BlockHandler::class,
                beanMethod = "block",
                operation = Operation(
                    tags = ["차단"],
                    summary = "유저 차단",
                    operationId = "blockMember",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = BlockHandler.BlockRequest::class
                            )
                        )]
                    ),
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
    fun blockRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/blocks", blockHandler::block)
        }
    }
}