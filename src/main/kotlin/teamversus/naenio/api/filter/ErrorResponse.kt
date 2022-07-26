package teamversus.naenio.api.filter

data class ErrorResponse(
    val code: String,
    val message: String,
) {
    companion object {
        fun badRequest(message: String?): ErrorResponse =
            ErrorResponse("FAIL", message ?: "잘못된 요청입니다.")

        fun error(message: String?): ErrorResponse =
            ErrorResponse("ERROR", message ?: "서버 오류")
    }
}
