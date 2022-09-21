package teamversus.naenio.api.domain.block.domain.model

data class Block(
    val id: Long = 0,
    val memberId: Long,
    val targetMemberId: Long,
) {
    fun withId(id: Long): Block = Block(id, memberId, targetMemberId)
}