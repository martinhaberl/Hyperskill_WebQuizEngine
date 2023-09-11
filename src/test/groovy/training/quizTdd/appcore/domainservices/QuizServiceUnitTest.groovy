package training.quizTdd.appcore.domainservices

import spock.lang.Specification
import training.quizTdd.appcore.domainmodel.Quiz

class QuizServiceUnitTest extends Specification {

    def 'should create a Quiz and store it'() {
        given: 'some values for a quiz'
        def title = 'q1title'
        def text = 'q1text'
        def options = ['q1opt1', 'q1opt2', 'q1opt3', 'q1opt4']
        def answer = 1
        and: 'a QuizService'
        IQuizService quizService = new QuizService();

        when: 'a quiz is created'
        def quiz = quizService.createQuiz(title, text, options, answer)

        then: 'a quiz instance is returned'
        quiz.getTitle() == title
        quiz.getText() == text
        quiz.getOptions() == options
        quiz.getAnswer() == answer
        and: 'Quiz is stored in Collection'
        quizService.getQuizzes().size() == 1
        quizService.getQuizzes().get(0).getTitle() == title
    }

    def "should return all stored quizzes"() {
        given: 'a quiz service'
        IQuizService quizService = new QuizService();
        and: 'first quiz'
        Quiz quiz1 = quizService.createQuiz('title', 'text', ['option'], 0)
        and: 'second quiz'
        Quiz quiz2 = quizService.createQuiz('title2', 'text2', ['option1'], 2)

        when: 'stored quizzes are requested'
        def quizzes = quizService.getQuizzes()

        then: 'all quizzes are returned'
        quizzes.size() == 2
        quizzes.get(0).getId() != null
        quizzes.get(1).getAnswer() == 2
    }

}
