package training.quizTdd.infrastructure

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification
import training.quizTdd.infrastructure.api.QuizController

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SecurityFilterChainTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @SpringBean
    QuizController controller = Mock()

    def "GET request to /api/quizzes should be open for everyone"() {
        when: "a GET request is made to /api/quizzes"
        def result = mockMvc.perform(get("/api/quizzes"))

        then: "the response status is OK"
        result.andExpect(status().isOk())
        and: "mocked controller method getAllQuizzes is called once"
        1 * controller.getAllQuizzes()
    }

    @Ignore
    //Fixme
    def "new visitors should be allowed to access /api/register for registration"() {
        when: 'a visitor tries to register'
        def result = mockMvc.perform(post('/api/register'))

        then: 'access is granted with http status 220'
        result.andExpect(status().is(200))
    }
}

