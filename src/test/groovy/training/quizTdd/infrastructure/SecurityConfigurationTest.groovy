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

    def "POST request to /api/register should be accessible for everyone"() {
        when: "a POST request is made to /api/register"
        def result = mockMvc.perform(post("/api/register"))

        then: "the response status is ok"
        result.andExpect(status().isOk())
        and: "mocked controller method getAllQuizzes is not called"
        1 * controller.register(_)
    }

    def "GET request to /api/quizzes should be blocked for non-authenticated users"() {
        when: "a GET request is made to /api/quizzes"
        def result = mockMvc.perform(get("/api/quizzes"))

        then: "the response status is unauthorized"
        result.andExpect(status().isUnauthorized())
        and: "mocked controller method getAllQuizzes is not called"
        0 * controller.getAllQuizzes()
    }

    @Ignore
    //fixme: authentication is missing
    def "GET request to /api/quizzes should be open for authenticated users"() {
        when: "a GET request is made to /api/quizzes"
        def result = mockMvc.perform(get("/api/quizzes"))

        then: "the response status is OK"
        result.andExpect(status().isOk())
        and: "mocked controller method getAllQuizzes is called once"
        1 * controller.getAllQuizzes()
    }

    @Ignore
    //fixme: authentication is missing
    def "GET request to /api/quizzes/{id} should be accessible for authenticated users"() {
        when: "a GET request is made to /api/quizzes/{id}"
        def result = mockMvc.perform(get("/api/quizzes/000"))

        then: "the response status is forbidden"
        result.andExpect(status().isForbidden())
        and: "mocked controller method getAllQuizzes is not called"
        0 * controller.getQuiz(000)
    }

    @Ignore
    //Fixme
    def "new visitors should be allowed to access /api/register for registration"() {
        when: 'a visitor tries to register'
        def result = mockMvc.perform(post('/api/register'))

        then: 'access is granted with http status 220'
        result.andExpect(status().is(200))
    }

    @Ignore
    //fixme: authentication is missing
    def "POST request to /api/quizzes should be accessible for authenticated users"() {
        when: "a POST request is made to /api/quizzes"
        def result = mockMvc.perform(post("/api/quizzes"))

        then: "the response status is ok"
        result.andExpect(status().isOk())
        and: "mocked controller method getAllQuizzes is not called"
        1 * controller.createQuiz(_)
    }

    @Ignore
    //fixme: authentication is missing
    def "POST request to /api/quizzes should be forbidden for non-authenticated users"() {
        when: "a POST request is made to /api/quizzes"
        def result = mockMvc.perform(post("/api/quizzes"))

        then: "the response status is forbidden"
        result.andExpect(status().isForbidden())
        and: "mocked controller method getAllQuizzes is not called"
        0 * controller.createQuiz(_)
    }
}

