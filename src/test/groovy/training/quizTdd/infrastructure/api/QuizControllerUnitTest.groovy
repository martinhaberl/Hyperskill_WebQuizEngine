package training.quizTdd.infrastructure.api

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import training.quizTdd.appcore.domainservices.IQuizService
import training.quizTdd.appcore.domainservices.QuizService
import training.quizTdd.infrastructure.api.dtos.AnswerResponseDto
import training.quizTdd.infrastructure.api.dtos.QuizRequestDto
import training.quizTdd.infrastructure.api.dtos.QuizResponseDto
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException

@SpringBootTest
class QuizControllerUnitTest extends Specification {

    def 'a post request creates a new Quiz and returns it'() {
        given: 'some values for a quiz'
        def title = 'q1title'
        def text = 'q1text'
        def options = ['q1opt1', 'q1opt2', 'q1opt3', 'q1opt4']
        def answer = 1
        and: 'a QuizService'
        def quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'a Quiz Request DTO'
        QuizRequestDto quizRequestDto = new QuizRequestDto(title, text, options, answer)

        when: 'a Quiz is created'
        QuizResponseDto responseDto = controller.createQuiz(quizRequestDto)

        then: 'a Quiz Response Dto is returned'
        responseDto.id() != null
        responseDto.title() == title
        responseDto.text() == text
        responseDto.options() == options
    }


    def 'a correct answer returns a positive feedback'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'a quiz to solve'
        quizService.createQuiz("title", "text", List.of("option", "option2"), 0)
        int quizId = quizService.getQuizzes().get(0).getId()
        and: 'a positive answer object'
        AnswerResponseDto answerResponseDto = new AnswerResponseDto(
                true, 'Congratulations, you\'re right!'
        )

        when: 'correct answer is given'
        def response = controller.solveQuiz(quizId, 0)

        then: 'positive feedback is given'
        response == answerResponseDto
    }

    def 'a wrong answer returns a negative feedback'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'some quiz to solve'
        quizService.createQuiz("title", "text", List.of("option", "option2"), 0)
        int quizId = quizService.getQuizzes().get(0).getId()
        and: 'a positive answer object'
        AnswerResponseDto answerResponseDto = new AnswerResponseDto(
                false, 'Wrong answer! Please, try again.'
        )

        when: 'correct answer is given'
        def response = controller.solveQuiz(quizId, 2)

        then: 'negative feedback is given'
        response == answerResponseDto
    }

    def 'answering a quiz that does not exists returns 404'() {
        given: 'a service to process quizzes'
        IQuizService quizService = new QuizService()
        and: 'a RestController for Quiz requests'
        QuizController controller = new QuizController(quizService)
        and: 'some quiz to solve'
        quizService.createQuiz("title", "text", List.of("option", "option2"), 0)
        and: 'some QuizId'
        def someQuizId = 1234567890

        when: 'an answer to a non-existing quiz is given'
        def response = controller.solveQuiz(someQuizId, 0)

        then: 'a QuizNotFoundException is triggered'
        thrown(QuizNotFoundException)


    }
}
