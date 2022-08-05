package teamversus.naenio.api.support

import org.springframework.data.domain.Pageable
import org.springframework.web.reactive.function.server.ServerRequest

fun ServerRequest.pageSizeInQueryParam(): Int =
    this.queryParam("size")
        .orElseThrow { IllegalArgumentException("사이즈는 필수값 입니다.") }
        .toInt()

fun ServerRequest.pageableOfSizeInQueryParam(): Pageable =
    Pageable.ofSize(pageSizeInQueryParam())

fun ServerRequest.lastPostIdInQueryParam(): Long =
    this.queryParam("lastPostId")
        .orElse(Long.MAX_VALUE.toString())
        .toLong()

fun ServerRequest.lastCommentIdInQueryParam(): Long =
    this.queryParam("lastCommentId")
        .orElse(Long.MAX_VALUE.toString())
        .toLong()

fun ServerRequest.pathVariableId(): Long =
    this.pathVariable("id")
        .toLong()