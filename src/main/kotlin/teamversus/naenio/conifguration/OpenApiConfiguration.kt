package teamversus.naenio.conifguration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "네니오 API 문서",
        description = "<p>이 사건은 재미있습니다. 제가 좋아하는 고래 퀴즈 같아요.</p>" +
                "<p>몸무게가 22톤인 암컷 향고래가 500kg에 달하는 대왕오징어를 먹고 6시간 뒤 1.3톤짜리 알을 낳았다면 이 암컷 향고래의 몸무게는 얼마일까요?</p>" +
                "<p>정답은 ‘고래는 알을 낳을 수 없다’입니다. 고래는 포유류라 알이 아닌 새끼를 낳으니까요.</p>" +
                "<p>무게에만 초점을 맞추면 문제를 풀 수 없습니다. 핵심을 봐야 돼요.</p>",
        version = "0.1.0"
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer "
)
@Configuration
class OpenApiConfiguration