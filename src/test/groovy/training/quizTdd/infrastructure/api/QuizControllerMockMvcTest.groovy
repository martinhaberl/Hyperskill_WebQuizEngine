package training.quizTdd.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll
import training.quizTdd.appcore.domainmodel.Quiz

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerMockMvcTest extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    def quizAContentJSON = '{\"title\": \"The Java Logo\", \"text\": \"What is depicted on the Java logo?\", \"options\": [\"Robot\", \"Tea leaf\", \"Cup of coffee\", \"Bug\"], \"answer\": [2]}'
    def quizBContentJSON = '{\"title\": \"The Apple Logo\",\"text\": \"What is depicted on the Apple logo?\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [2, 3]}'

    def "should create a quiz"() {
        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizAContentJSON)

        then: 'quiz is created'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value("The Java Logo"))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.text').value("What is depicted on the Java logo?"))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.options').value(["Robot", "Tea leaf", "Cup of coffee", "Bug"]))
    }

    def "should return all quizzes"() {
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
        requestedQuiz.andReturn().response.status == 200
        requestedQuiz.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNotEmpty())
        requestedQuiz.andExpect(MockMvcResultMatchers.jsonPath('$.title').value('The Apple Logo'))
    }

    def "when creating a quiz, a missing title should return a bad-request-status"() {
        given: 'a quiz without title as JSON string'
        def quizContentMissingTitle = '{\"text\": \"What is depicted on the Apple logo?\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentMissingTitle)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, an empty title should return a bad-request-exception"() {
        given: 'a quiz with empty title as JSON string'
        def quizContentEmptyTitle = '{\"title\": \"\",\"text\": \"What is depicted on the Apple logo?\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentEmptyTitle)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, missing answers should return a bad-request-exception"() {
        given: 'a quiz with empty answer options as JSON string'
        def quizContentEmptyOptions = '{\\"title\\": \\"The Apple Logo\\",\\"text\\": \\"What is depicted on the Apple logo?\\",\\"options\\": [],\\"answer\\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentEmptyOptions)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    @Unroll
    def "solving an existing quiz with a correct answer should return a positive feedback"() {
        given: 'a quiz with 2 correct answers'
        def quizWith2correctAnswers = createQuiz(quizBContentJSON)
        and: 'created quiz as quiz object'
        def twoAnswerQuiz = objectMapper.readValue(quizWith2correctAnswers.andReturn().getResponse().getContentAsString(), Quiz.class)
        def twoAnswerQuizId = twoAnswerQuiz.id

        when: 'a correct answer is given'
        def result = solveQuiz(twoAnswerQuizId, answer)

        then: 'a positive feedback is returned'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.success').value(true))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.feedback').value('Congratulations, you\'re right!'))

        where:
        answer | _
        2      | _
        3      | _
    }

    @Unroll
    def "solving an existing quiz with a wrong answer should returns a negative feedback"() {
        given: 'a quiz'
        def quiz = createQuiz(quizBContentJSON)
        Quiz createdQuiz = objectMapper.readValue(quiz.andReturn().getResponse().getContentAsString(), Quiz.class)
        and: 'id of created quiz'
        def createdId = createdQuiz.getId()

        when: 'wrong answer is given'
        def result = solveQuiz(createdQuiz.getId(), wrongAnswer)

        then: 'negative feedback is returned'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.success').value(false))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.feedback').value('Wrong answer! Please, try again.'))

        where:
        wrongAnswer | _
        1234        | _
        4321        | _
        4711        | _
        11          | _
    }

    @Unroll
    def "solving a non-existing quiz should return a not-found-status"() {
        when: 'an invalid quizId is received'
        def result = solveQuiz(quizId, 0)

        then: 'a not-found-status is returned'
        result.andReturn().response.status == 404

        where:
        quizId | _
        123456 | _
        987654 | _
        741852 | _
        96315  | _
    }

    private ResultActions solveQuiz(int quizId, int answerIndex) {
        def url = '/api/quizzes/' + quizId + '/solve?answer=' + answerIndex
        return mvc.perform(MockMvcRequestBuilders.post(url))
    }

    private createQuiz(String quizRequestJson) {
        mvc.perform(MockMvcRequestBuilders
                .post('/api/quizzes')
                .content(quizRequestJson)
                .contentType(MediaType.APPLICATION_JSON))
    }

}
