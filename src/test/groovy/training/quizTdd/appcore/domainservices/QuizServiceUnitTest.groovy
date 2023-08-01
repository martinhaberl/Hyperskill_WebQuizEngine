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
}
