package teamversus.naenio.api.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import teamversus.naenio.api.domain.member.application.service.TokenException

@Order(-2)
@Component
class GlobalExceptionHandler : ErrorWebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        if (ex is TokenException) {
            return exchange.response.writeWith(Mono.fromSupplier {
                val bufferFactory = exchange.response.bufferFactory()
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                return@fromSupplier bufferFactory.wrap(objectMapper.writeValueAsBytes(ErrorResponse.unauthorized(ex.message)))
            })
        }

        if (ex is IllegalArgumentException) {
            log.warn("bad request", ex)
            return exchange.response.writeWith(Mono.fromSupplier {
                val bufferFactory = exchange.response.bufferFactory()
                exchange.response.statusCode = HttpStatus.BAD_REQUEST
                return@fromSupplier bufferFactory.wrap(objectMapper.writeValueAsBytes(ErrorResponse.badRequest(ex.message)))
            })
        }

        log.error("error", ex)

        return exchange.response.writeWith(Mono.fromSupplier {
            val bufferFactory = exchange.response.bufferFactory()
            exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            return@fromSupplier bufferFactory.wrap(objectMapper.writeValueAsBytes(ErrorResponse.error(ex.message)))
        })
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private val objectMapper = ObjectMapper()
    }
}