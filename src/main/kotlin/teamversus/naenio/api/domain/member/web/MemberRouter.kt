package teamversus.naenio.api.domain.member.web

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
class MemberRouter(private val memberHandler: MemberHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/login",
                method = [RequestMethod.POST],
                beanClass = MemberHandler::class,
                beanMethod = "login",
                operation = Operation(
                    tags = ["회원"],
                    summary = "OAuth 로그인",
                    operationId = "login",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = MemberHandler.LoginRequest::class
                            )
                        )]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = MemberHandler.LoginResponse::class))]
                        )
                    ]
                )
            ),
            RouterOperation(
                path = "/app/members/nickname",
                method = [RequestMethod.PUT],
                beanClass = MemberHandler::class,
                beanMethod = "setNickname",
                operation = Operation(
                    tags = ["회원"],
                    summary = "닉네임 설정",
                    operationId = "setNickname",
                    requestBody = RequestBody(
                        content = [Content(schema = Schema(implementation = MemberHandler.SetNicknameRequest::class))]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = MemberHandler.SetNicknameResponse::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            ),
            RouterOperation(
                path = "/app/members/exist",
                method = [RequestMethod.GET],
                beanClass = MemberHandler::class,
                beanMethod = "exist",
                operation = Operation(
                    tags = ["회원"],
                    summary = "닉네임 중복 체크",
                    operationId = "existByNickname",
                    parameters = [Parameter(
                        name = "nickname",
                        `in` = ParameterIn.QUERY,
                        required = true
                    )],
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = MemberHandler.ExistResponse::class))]
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            )],

        )
    fun memberRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/login", memberHandler::login)
            PUT("/app/members/nickname", memberHandler::setNickname)
            GET("/app/members/exist", memberHandler::exist)
        }
    }
}