package training.quizTdd.infrastructure.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerMockMvcTest extends Specification {

    @Autowired
    MockMvc mvc

    def "should create a quiz"() {
        given: 'a quiz as JSON string'
        def quizContent = '{}'

        when: 'a request is sent to create a quiz'
        def result = mvc.perform(MockMvcRequestBuilders
                .post('/api/quizzes')
                .content('{\"title\": \"The Java Logo\",\"text\": \"What is depicted on the Java logo?\",\"options\": [\"Robot\", \"Tea leaf\", \"Cup of coffee\", \"Bug\"],\"answer\": 2}')
                .contentType(MediaType.APPLICATION_JSON))

        then: 'quiz is created'
        result.andExpect(MockMvcResultMatchers.status().isCreated())
    }


}
