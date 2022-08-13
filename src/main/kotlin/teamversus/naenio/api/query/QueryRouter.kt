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
import teamversus.naenio.api.query.fetcher.*
import teamversus.naenio.api.query.model.Theme
import teamversus.naenio.api.query.result.*

@Configuration
class QueryRouter(
    private val webPostFetcher: WebPostFetcher,
    private val appFeedSortTypeFetcher: AppFeedSortTypeFetcher,
    private val appThemeFetcher: AppThemeFetcher,
    private val appMemberFetcher: AppMemberFetcher,
    private val appFeedFetcher: AppFeedFetcher,
    private val appCommentFetcher: AppCommentFetcher,
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
                path = "/app/feed",
                method = [RequestMethod.GET],
                beanClass = AppFeedFetcher::class,
                beanMethod = "findFeed",
                operation = Operation(
                    tags = ["피드"],
                    summary = "피드조회",
                    operationId = "findFeed",
                    parameters = [
                        Parameter(
                            name = "size",
                            `in` = ParameterIn.QUERY,
                            required = true
                        ),
                        Parameter(
                            name = "lastPostId",
                            `in` = ParameterIn.QUERY,
                            required = false,
                            description = "첫 조회, 새로고침의 경우 lastPostId를 비워서 보내면 됩니다."
                        ),
                    ],
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = AppFeedQueryResult::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/posts/{id}/comments",
                method = [RequestMethod.GET],
                beanClass = AppCommentFetcher::class,
                beanMethod = "findPostComments",
                operation = Operation(
                    tags = ["댓글"],
                    summary = "게시글 댓글 조회",
                    operationId = "findPostComments",
                    parameters = [
                        Parameter(
                            name = "id",
                            `in` = ParameterIn.PATH,
                            required = true,
                            description = "게시글 아이디"
                        ),
                        Parameter(
                            name = "size",
                            `in` = ParameterIn.QUERY,
                            required = true
                        ),
                        Parameter(
                            name = "lastCommentId",
                            `in` = ParameterIn.QUERY,
                            required = false,
                            description = "첫 조회, 새로고침의 경우 lastCommentId를 비워서 보내면 됩니다."
                        ),
                    ],
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = AppPostCommentsQueryResult::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/comments/{id}/comment-replies",
                method = [RequestMethod.GET],
                beanClass = AppCommentFetcher::class,
                beanMethod = "findCommentReplies",
                operation = Operation(
                    tags = ["댓글"],
                    summary = "대댓글 조회",
                    operationId = "findCommentReplies",
                    parameters = [
                        Parameter(
                            name = "id",
                            `in` = ParameterIn.PATH,
                            required = true,
                            description = "댓글 아이디"
                        ),
                        Parameter(
                            name = "size",
                            `in` = ParameterIn.QUERY,
                            required = true
                        ),
                        Parameter(
                            name = "lastCommentId",
                            `in` = ParameterIn.QUERY,
                            required = false,
                            description = "첫 조회, 새로고침의 경우 lastCommentId를 비워서 보내면 됩니다."
                        ),
                    ],
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = AppCommentRepliesQueryResult::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/members/me",
                method = [RequestMethod.GET],
                beanClass = AppMemberFetcher::class,
                beanMethod = "findMe",
                operation = Operation(
                    tags = ["회원"],
                    summary = "회원 정보 조회",
                    operationId = "findMe",
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = AppMemberQueryResult::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/feed/sort-types",
                method = [RequestMethod.GET],
                beanClass = AppFeedSortTypeFetcher::class,
                beanMethod = "findAll",
                operation = Operation(
                    tags = ["피드"],
                    summary = "피드 정렬 타입 목록 조회",
                    operationId = "findAllFeedSortTypes",
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = AppFeedSortTypeQueryResult::class))]
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
            GET("/app/feed/sort-types", appFeedSortTypeFetcher::findAll)
            GET("/app/members/me", appMemberFetcher::findMe)
            GET("/app/themes", appThemeFetcher::findAll)
            GET("/app/feed", appFeedFetcher::findFeed)
            GET("/app/posts/{id}/comments", appCommentFetcher::findPostComments)
            GET("/app/comments/{id}/comment-replies", appCommentFetcher::findCommentReplies)
        }
    }
}