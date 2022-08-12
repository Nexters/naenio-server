package teamversus.naenio.api.domain.report.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class ReportRouter(reportHandler: ReportHandler) {
    @Bean
    fun reportRouterFunction(): RouterFunction<ServerResponse> = router {

    }
}