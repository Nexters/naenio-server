package teamversus.naenio.api.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
class QueryRouter(
    private val webPostFetcher: WebPostFetcher,
) {
    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/web/posts/\${id}",
            method = [RequestMethod.GET],
            beanClass = WebPostFetcher::class,
            beanMethod = "findDetailById",
            operation = Operation(
                tags = ["게시글"],
                summary = "게시글 상세 조회 (웹 용)",
                operationId = "findDetailById",
                parameters = [Parameter(
                    name = "id",
                    `in` = ParameterIn.PATH,
                    required = true
                )],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = WebPostDetailQueryResult::class))]
                    )
                ],
            )
        )
    )
    fun queryRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/web/posts", webPostFetcher::findDetailById)
        }
    }
}