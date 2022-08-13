package teamversus.naenio.api.domain.report.web

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
class ReportRouter(private val reportHandler: ReportHandler) {
    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/app/reports",
                method = [RequestMethod.POST],
                beanClass = ReportHandler::class,
                beanMethod = "register",
                operation = Operation(
                    tags = ["신고"],
                    summary = "신고하기",
                    operationId = "registerReport",
                    requestBody = RequestBody(
                        content = [Content(
                            schema = Schema(
                                implementation = ReportHandler.RegisterReportRequest::class
                            )
                        )]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "204"
                        )
                    ],
                    security = [SecurityRequirement(name = "Bearer Authentication")]
                )
            )
        ]
    )
    fun reportRouterFunction(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            POST("/app/reports", reportHandler::register)
        }
    }
}
