package training.quizTdd.appcore.domainservices


import spock.lang.Specification
import training.quizTdd.appcore.domainmodel.Quiz
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException

class QuizServiceUnitTest extends Specification {

    def 'should create a Quiz and store it'() {
        given: 'some values for a quiz'
        def title = 'q1title'
        def text = 'q1text'
        def options = ['q1opt1', 'q1opt2', 'q1opt3', 'q1opt4']
        def answer = 1
        and: 'a QuizService'
        QuizService quizService = new QuizService();

        when: 'a quiz is created'
        Quiz quiz = quizService.createQuiz(title, text, options, answer)

        then: 'a quiz instance is returned'
        quiz.getTitle() == title
        quiz.getText() == text
        quiz.getOptions() == options
        quiz.getAnswer() == answer
        and: 'Quiz is stored in Collection'
        quizService.getQuizzes().size() == 1
        quizService.getQuizzes().get(0).getTitle() == title
    }

    def "should return index of stored quiz when valid quiz id is requested"() {
        given: 'a quiz service'
        QuizService quizService = new QuizService();
        and: 'a quiz'
        Quiz quiz = quizService.createQuiz('title', 'text', ['option'], 0)
        and: 'generated id of quiz'
        int quizId = quizService.getQuizzes().get(0).getId()

        when: 'index of quiz is requested '
        def index = quizService.getIndex(quizId)

        then: 'index of stored quiz is returned'
        index == 0

    }

    def "should return exception when non-existing quiz id is requested"() {
        given: 'a quiz service'
        QuizService quizService = new QuizService();
        and: 'a quiz'
        Quiz quiz = quizService.createQuiz('title', 'text', ['option'], 0)
        and: 'generated id of quiz'
        int quizId = 1234567890

        when: 'non-existing quiz id is requested'
        def index = quizService.getIndex(quizId)

        then: 'exception is thrown'
        thrown(QuizNotFoundException)
    }

}
