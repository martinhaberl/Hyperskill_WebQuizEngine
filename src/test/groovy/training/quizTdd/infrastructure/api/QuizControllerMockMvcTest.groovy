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
        result.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNotEmpty())
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

    def "when creating a quiz, a request without title should return a bad-request-status"() {
        given: 'a quiz without title as JSON string'
        def quizContentMissingTitle = '{\"text\": \"What is depicted on the Apple logo?\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentMissingTitle)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, a request with an empty title should return a bad-request-exception"() {
        given: 'a quiz with empty title as JSON string'
        def quizContentEmptyTitle = '{\"title\": \"\",\"text\": \"What is depicted on the Apple logo?\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentEmptyTitle)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, a request without question should return a bad-request-exception"() {
        given: 'a quiz without question as JSON string'
        def quizContentWithoutQuestion = '{\"title\": \"The Apple Logo\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [2, 3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentWithoutQuestion)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, a request with an empty question should return a bad-request-exception"() {
        given: 'a quiz with empty question as JSON string'
        def quizContentEmptyQuestion = '{\"title\": \"\",\"text\": \"\",\"options\": [\"Peach\", \"Drum\", \"Quokka\", \"Apple\"],\"answer\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentEmptyQuestion)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, a request without answer-options should return a bad-request-exception"() {
        given: 'a quiz with empty answer-options as JSON string'
        def quizContentEmptyOptions = '{\\"title\\": \\"The Apple Logo\\",\\"text\\": \\"What is depicted on the Apple logo?\\",\\"options\\": [],\\"answer\\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentEmptyOptions)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, a request with less than 2 answer-options should return a bad-request-exception"() {
        given: 'a quiz with only 1 answer-option as JSON string'
        def quizContentTooFewOptions = '{\\"title\\": \\"The Apple Logo\\",\\"text\\": \\"What is depicted on the Apple logo?\\",\\"options\\": [\"Quokka\"],\\"answer\\": [3]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentTooFewOptions)

        then: 'bad-request-status is returned'
        result.andReturn().response.status == 400
    }

    def "when creating a quiz, a request without answers should return a valid quiz"() {
        given: 'a quiz without answers as JSON string'
        def quizContentNoAnswers = '{\"title\":\"The Apple Logo\",\"text\":\"What is depicted on the Apple logo?\",\"options\":[\"Peach\",\"Drum\",\"Quokka\",\"Bumblebee\"]}'

        when: 'a request is sent to create a quiz'
        def result = createQuiz(quizContentNoAnswers)

        then: 'valid quiz is returned'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNotEmpty())
        result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value("The Apple Logo"))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.text').value("What is depicted on the Apple logo?"))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.options').value(["Peach", "Drum", "Quokka", "Bumblebee"]))
    }

    @Unroll
    def "solving an existing quiz with a correct answer should return a positive feedback"() {
        given: 'a quiz with 2 correct answers'
        def quizWith2correctAnswersAsJson = createQuiz(quizBContentJSON).andReturn()
                .getResponse()
                .getContentAsString()
        and: 'created quiz as quiz object'
        def createdQuizWith2CorrectAnswers = objectMapper.readValue(quizWith2correctAnswersAsJson, Quiz.class)

        when: 'a correct answer is given'
        def correctAnswersJson = '{ \"answer\": %s }'.formatted(correctAnswer)
        def result = requestToSolveQuiz(createdQuizWith2CorrectAnswers.id.toString(), correctAnswersJson)

        then: 'a positive feedback is returned'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.success').value(true))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.feedback').value('Congratulations, you\'re right!'))

        where:
        correctAnswer | _
        '[2,3]'       | _
        '[3,2]'       | _
    }

    @Unroll
    def "solving a non-existing quiz should return a not-found-status"() {
        when: 'an invalid quizId is received'
        def result = requestToSolveQuiz(quizId, '{ \"answer\": [0,1]}')

        then: 'a not-found-status is returned'
        result.andReturn().response.status == 404

        where:
        quizId                                             | _
        (Math.random() * 100000).toLong().abs().toString() | _
        (Math.random() * 100000).toLong().abs().toString() | _
        (Math.random() * 100000).toLong().abs().toString() | _
    }

    @Unroll
    def "solving an existing quiz with a wrong answer should return a negative feedback"() {
        given: 'a quiz'
        def quizAsString = createQuiz(quizBContentJSON).andReturn()
                .getResponse().getContentAsString()
        Quiz createdQuiz = objectMapper.readValue(quizAsString, Quiz.class)

        when: 'wrong answer is sent'
        def wrongAnswersJson = '{ \"answer\": %s }'.formatted(wrongAnswer)
        def result = requestToSolveQuiz(createdQuiz.getId().toString(), wrongAnswersJson)

        then: 'negative feedback is returned'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.success')
                .value(false))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.feedback')
                .value('Wrong answer! Please, try again.'))

        where:
        wrongAnswer | _
        [1234]      | _
        [4321, 34]  | _
        [4711]      | _
        [11]        | _
    }

    def "solving an existing quiz without answers should return a positive feedback"() {
        given: 'a quiz without answers'
        def quizContentNoAnswers = '{\"title\":\"The Apple Logo\",\"text\":\"What is depicted on the Apple logo?\",\"options\":[\"Peach\",\"Drum\",\"Quokka\",\"Bumblebee\"]}'
        def quizAsString = createQuiz(quizContentNoAnswers).andReturn()
                .getResponse().getContentAsString()
        Quiz createdQuiz = objectMapper.readValue(quizAsString, Quiz.class)

        when: 'correct empty answer is sent'
        def emptyAnswersJson = '{ \"answer\": [] }'
        def result = requestToSolveQuiz(createdQuiz.getId().toString(), emptyAnswersJson)

        then: 'positive feedback is returned'
        result.andReturn().response.status == 200
        result.andExpect(MockMvcResultMatchers.jsonPath('$.success')
                .value(true))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.feedback')
                .value('Congratulations, you\'re right!'))
    }

    private requestToSolveQuiz(String quizId, String answersJson) {
        def url = '/api/quizzes/%s/solve'.formatted(quizId)
        mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(answersJson)
                .contentType(MediaType.APPLICATION_JSON))
    }

    private createQuiz(String quizRequestJson) {
        mvc.perform(MockMvcRequestBuilders
                .post('/api/quizzes')
                .content(quizRequestJson)
                .contentType(MediaType.APPLICATION_JSON))
    }

}
