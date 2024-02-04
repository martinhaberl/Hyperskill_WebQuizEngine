package training.quizTdd.infrastructure.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import training.quizTdd.appcore.domainservices.IQuizRepository
import training.quizTdd.appcore.domainservices.IQuizService
import training.quizTdd.appcore.domainservices.QuizService
import training.quizTdd.infrastructure.api.dtos.AnswerResponseDto
import training.quizTdd.infrastructure.api.dtos.AnswersRequestDto
import training.quizTdd.infrastructure.api.dtos.QuizRequestDto
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException
import training.quizTdd.infrastructure.persistence.AppUserRepository

@SpringBootTest
class QuizControllerIntegrationTest extends Specification {

    @Autowired
    IQuizRepository iQuizRepository

    @Autowired
    AppUserRepository appUserRepository
    @Autowired
    PasswordEncoder passwordEncoder

    def title = 'someTitle'
    def text = 'someText'
    def options = ['option1', 'option2', 'option3', 'option4']
    def answer = [1]

    def 'a post request creates a new Quiz and returns it'() {
        given: 'some values for a quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)

        when: 'a Quiz is created'
        def response = controller.createQuiz(quizRequestDto)

        then: 'a Quiz Response Dto is returned'
        response.getStatusCode().value() == 200
        response.body.id() != null
        response.body.title() == title
        response.body.text() == text
        response.body.options() == options
    }

    def 'a get request returns all known quizzes'() {
        given: 'some values for a quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)
        and: '2 quizzes'
        controller.createQuiz(quizRequestDto)
        controller.createQuiz(quizRequestDto)

        when: 'all known quizzes are requested'
        def response = controller.getAllQuizzes()

        then: 'number of returned quizzes is 2'
        response.getStatusCode().value() == 200
    }


    def 'a get request with a specific quiz id returns specific quiz'() {
        given: 'some values for specific quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)
        and: '1 quiz'
        controller.createQuiz(quizRequestDto)
        and: 'id of created quiz'
        def quizId = quizService.getQuizzes().get(0).getId()

        when: 'quiz is requested by id'
        def response = controller.getQuiz(quizId)

        then: 'title of returned quiz is "title"'
        response.getStatusCode().value() == 200
        response.body.title().contains('qu')
    }

    def 'a get request with an invalid quiz id returns 404'() {
        given: 'some values for specific quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)
        and: '1 quiz'
        controller.createQuiz(quizRequestDto)
        and: 'an invalid id of created quiz'
        def quizId = (Math.random() * 10000).toLong().abs()

        when: 'quiz is requested by invalid id'
        controller.getQuiz(quizId)

        then: 'response 404 is given'
        thrown(QuizNotFoundException)

    }

    def 'a correct answer returns a positive feedback'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'a quiz to solve'
        def quizToSolve = quizService.createQuiz("title", "text", List.of("option", "option2"), [0, 1])
        def quizId = quizToSolve.id
        and: 'a request with 2 correct answers'
        AnswersRequestDto answersRequestDto = new AnswersRequestDto(List.of(0, 1))
        and: 'a positive answer object'
        AnswerResponseDto answerResponseDto = new AnswerResponseDto(
                true, 'Congratulations, you\'re right!'
        )

        when: 'correct answer is given'
        def response = controller.solveQuiz(quizId, answersRequestDto)

        then: 'positive feedback is given'
        response.getStatusCode().value() == 200
        response.getBody() == answerResponseDto
    }

    def 'a wrong answer returns a negative feedback'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'some quiz to solve'
        def quizToSolve = quizService.createQuiz("title", "text", List.of("option", "option2"), [0])
        def quizId = quizToSolve.id
        and: 'a request with 2 wrong answers'
        AnswersRequestDto answersRequestDto = new AnswersRequestDto(List.of(777, 666))
        and: 'a negative answer object'
        AnswerResponseDto answerResponseDto = new AnswerResponseDto(
                false, 'Wrong answer! Please, try again.'
        )

        when: 'correct answer is given'
        def response = controller.solveQuiz(quizId, answersRequestDto)

        then: 'negative feedback is given'
        response.getStatusCode().value() == 200
        response.getBody() == answerResponseDto
    }

    def 'answering a quiz that does not exist returns 404'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService(iQuizRepository)
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService, appUserRepository, passwordEncoder)
        and: 'some request with an answer'
        AnswersRequestDto answersRequestDto = new AnswersRequestDto(List.of())
        and: 'some QuizId'
        def someQuizId = (Math.random() * 10000).toLong().abs()

        when: 'an answer to a non-existing quiz is given'
        controller.solveQuiz(someQuizId, answersRequestDto)

        then: 'a QuizNotFoundException is triggered'
        thrown(QuizNotFoundException)
    }

}
