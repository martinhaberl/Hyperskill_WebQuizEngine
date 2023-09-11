package training.quizTdd.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import training.quizTdd.appcore.domainmodel.Quiz

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerMockMvcTest extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    def quizAContentJSON = '{\"title\": \"The Java Logo\", \"text\": \"What is depicted on the Java logo?\", \"options\": [\"Robot\", \"Tea leaf\", \"Cup of coffee\", \"Bug\"], \"answer\": 2}'
    def quizBContentJSON = '{\"title\": \"The Apple Logo\",\"text\": \"What is depicted on the Apple logo?\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": 3}'

    def "should create a quiz"() {
        given: 'a quiz as JSON string'
        quizAContentJSON

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizAContentJSON)

        then: 'quiz is created'
        result.andReturn().response.status == 200
        result.andReturn().response.contentAsString.contains("The Java Logo")

    }

    def "should create and return a quiz"() {
        given: 'a quiz as JSON string'
        quizAContentJSON
        and: 'a quiz'
        createQuiz(quizAContentJSON)

        when: 'all existing quizzes are requested'
        def result = mvc.perform(MockMvcRequestBuilders.get('/api/quizzes'))

        then: 'all quizzes are returned'
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        and: 'returned quiz is not empty'
        result.andExpect(MockMvcResultMatchers.jsonPath('$[0].id').isNotEmpty())
        result.andReturn().getResponse().getContentAsString().contains("The Java Logo")
    }

    def "should return a specific quiz"() {
        given: 'a quiz'
        def quiz = createQuiz(quizBContentJSON)

        Quiz createdQuiz = objectMapper.readValue(quiz.andReturn().getResponse().getContentAsString(), Quiz.class)

        when: 'specific quiz is requested by id'

        def createdId = createdQuiz.getId()
        def requestedQuiz = mvc.perform(MockMvcRequestBuilders.get('/api/quizzes/{id}', createdId))

        then: 'specific quiz with id is returned'
        requestedQuiz.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNotEmpty())
        requestedQuiz.andExpect(MockMvcResultMatchers.jsonPath('$.title').value('The Apple Logo'))
    }

    private createQuiz(String quizAContentJSONparam) {
        mvc.perform(MockMvcRequestBuilders
                .post('/api/quizzes')
                .content(quizAContentJSONparam)
                .contentType(MediaType.APPLICATION_JSON))
    }


}
