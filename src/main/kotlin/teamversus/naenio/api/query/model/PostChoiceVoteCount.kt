package teamversus.naenio.api.query.model

import org.springframework.data.annotation.Id

data class PostChoiceVoteCount(
    @Id
    val id: Long = 0,
    val postId: Long,
    val firstChoiceCount: Int = 0,
    val secondChoiceCount: Int = 0,
    val totalCount: Int = 0,
    val rate: Double,
) {

    constructor(id: Long, postId: Long, firstChoiceCount: Int, secondChoiceCount: Int) : this(
        id,
        postId,
        firstChoiceCount,
        secondChoiceCount,
        firstChoiceCount + secondChoiceCount,
        if (firstChoiceCount == 0 || secondChoiceCount == 0) 0.0 else
            Integer.min(firstChoiceCount, secondChoiceCount).toDouble() /
                    Integer.max(firstChoiceCount, secondChoiceCount).toDouble()
    )

    fun increaseFirst(): PostChoiceVoteCount =
        PostChoiceVoteCount(id, postId, firstChoiceCount + 1, secondChoiceCount)

    fun increaseSecond(): PostChoiceVoteCount =
        PostChoiceVoteCount(id, postId, firstChoiceCount, secondChoiceCount + 1)
}