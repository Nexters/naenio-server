package teamversus.naenio.api.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
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
import teamversus.naenio.api.query.fetcher.AppCategoryFetcher
import teamversus.naenio.api.query.fetcher.AppThemeFetcher
import teamversus.naenio.api.query.model.Theme
import teamversus.naenio.api.query.result.AppCategoriesQueryResult
import teamversus.naenio.api.query.result.WebPostDetailQueryResult

@Configuration
class QueryRouter(
    private val webPostFetcher: WebPostFetcher,
    private val appCategoryFetcher: AppCategoryFetcher,
    private val appThemeFetcher: AppThemeFetcher,
) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/web/posts/{id}",
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
            ),
            RouterOperation(
                path = "/app/categories",
                method = [RequestMethod.GET],
                beanClass = AppCategoryFetcher::class,
                beanMethod = "findAll",
                operation = Operation(
                    tags = ["카테고리"],
                    summary = "카테고리 목록 조회",
                    operationId = "findAllCategory",
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = AppCategoriesQueryResult::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/themes",
                method = [RequestMethod.GET],
                beanClass = AppThemeFetcher::class,
                beanMethod = "findAll",
                operation = Operation(
                    tags = ["테마"],
                    summary = "테마 목록 조회",
                    operationId = "findAllTheme",
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = Theme::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            )
        ]
    )
    fun queryRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/web/posts/{id}", webPostFetcher::findDetailById)
            GET("/app/categories", appCategoryFetcher::findAll)
            GET("/app/themes", appThemeFetcher::findAll)
        }
    }
}