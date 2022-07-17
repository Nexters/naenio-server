package teamversus.naenio.api.core.post.application.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import teamversus.naenio.api.core.post.application.PostCreateUseCase

@ActiveProfiles("local")
@SpringBootTest
internal class PostServiceTest {

    @Autowired
    private lateinit var postService: PostService

    @Test
    fun create() {
        postService.create(
            PostCreateUseCase.Command(
                "asd",
                "asdasd",
                listOf(
                    PostCreateUseCase.Command.ChoiceCommand(
                        "asd"
                    ),
                    PostCreateUseCase.Command.ChoiceCommand(
                        "asd2"
                    )
                )
            ), 1L
        ).block()
    }
}