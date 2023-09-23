package training.quizTdd.infrastructure.api

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import training.quizTdd.appcore.domainservices.IQuizService
import training.quizTdd.appcore.domainservices.QuizService
import training.quizTdd.infrastructure.api.dtos.AnswerResponseDto
import training.quizTdd.infrastructure.api.dtos.QuizRequestDto
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException

@SpringBootTest
class QuizControllerIntegrationText extends Specification {

    def title = 'someTitle'
    def text = 'someText'
    def options = ['option1', 'option2', 'option3', 'option4']
    def answer = [1]

    def 'a post request creates a new Quiz and returns it'() {
        given: 'some values for a quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
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
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)
        and: '2 quizzes'
        controller.createQuiz(quizRequestDto)
        controller.createQuiz(quizRequestDto)

        when: 'all known quizzes are requested'
        def response = controller.getAllQuizzes()

        then: 'number of returned quizzes is 2'
        response.getStatusCode().value() == 200
        response.body.size() == 2
        response.body.get(1).id() > 0
        response.body.get(1).options().size() == 4
    }


    def 'a get request with a specific quiz id returns specific quiz'() {
        given: 'some values for specific quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
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
        response.body.title() == 'someTitle'
    }

    def 'a get request with an invalid quiz id returns 404'() {
        given: 'some values for specific quiz'
        and: 'a QuizService'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)
        and: '1 quiz'
        controller.createQuiz(quizRequestDto)
        and: 'an invalid id of created quiz'
        def quizId = 1234567890

        when: 'quiz is requested by invalid id'
        controller.getQuiz(quizId)

        then: 'response 404 is given'
        thrown(QuizNotFoundException)

    }

    def 'a correct answer returns a positive feedback'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'a quiz to solve'
        quizService.createQuiz("title", "text", List.of("option", "option2"), [0])
        int quizId = quizService.getQuizzes().get(0).getId()
        and: 'a positive answer object'
        AnswerResponseDto answerResponseDto = new AnswerResponseDto(
                true, 'Congratulations, you\'re right!'
        )

        when: 'correct answer is given'
        def response = controller.solveQuiz(quizId, 0)

        then: 'positive feedback is given'
        response.getStatusCode().value() == 200
        response.getBody() == answerResponseDto
    }

    def 'a wrong answer returns a negative feedback'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'some quiz to solve'
        quizService.createQuiz("title", "text", List.of("option", "option2"), [0])
        int quizId = quizService.getQuizzes().get(0).getId()
        and: 'a positive answer object'
        AnswerResponseDto answerResponseDto = new AnswerResponseDto(
                false, 'Wrong answer! Please, try again.'
        )

        when: 'correct answer is given'
        def response = controller.solveQuiz(quizId, 2)

        then: 'negative feedback is given'
        response.getStatusCode().value() == 200
        response.getBody() == answerResponseDto
    }

    def 'answering a quiz that does not exist returns 404'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'some quiz to solve'
        quizService.createQuiz("title", "text", List.of("option", "option2"), [0])
        and: 'some QuizId'
        def someQuizId = 1234567890

        when: 'an answer to a non-existing quiz is given'
        controller.solveQuiz(someQuizId, 0)

        then: 'a QuizNotFoundException is triggered'
        thrown(QuizNotFoundException)
    }

}
