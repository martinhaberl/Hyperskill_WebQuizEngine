package training.quizTdd.appcore.domainservices

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll
import training.quizTdd.appcore.domainmodel.Answer
import training.quizTdd.appcore.domainmodel.Quiz

@SpringBootTest
class QuizServiceTest extends Specification {

    @Autowired
    IQuizService quizService

    @Unroll
    def 'should create a Quiz and store it'() {
        when: 'a quiz is created'
        def quiz = quizService.createQuiz(title, question, options, answer)

        then: 'a quiz instance is returned'
        quiz.getTitle() == title
        quiz.getText() == question
        quiz.getOptions() == options
        quiz.getAnswer() == answer

        where:
        title        | question        | options                                       | answer
        'quizTitle1' | 'quizQuestion1' | ['option1', 'option2', 'option3']             | [0]
        'quizTitle2' | 'quizQuestion2' | ['option1', 'option2', 'option3', 'options4'] | [1]
        'quizTitle3' | 'quizQuestion3' | ['option1', 'option2']                        | [0, 1]
        'quizTitle4' | 'quizQuestion4' | ['option1', 'option2', 'option3', 'option4']  | [1, 0, 3]

    }

    def "should return all stored quizzes"() {
        given: '2 quizzes'
        quizService.createQuiz('title1', 'question', ['option'], [0])
        quizService.createQuiz('title2', 'question2', ['option1'], [2])

        when: 'stored quizzes are requested'
        def quizzes = quizService.getQuizzes()

        then: 'all quizzes are returned'
        quizzes.size() >= 2
        quizzes.get(1).id.getClass() == Long
        !quizzes.get(1).answer.isEmpty()
    }

    def 'should return specific quiz by id'() {
        given: 'a quiz'
        quizService.createQuiz('title1', 'question', ['option'], [0])
        and: 'its generated quizId'
        def quizId = quizService.getQuizzes().get(0).getId()

        when:
        def specificQuiz = quizService.getQuiz(quizId)

        then:
        specificQuiz.id == quizId
        specificQuiz.options == quizService.getQuizzes().get(0).getOptions()
    }


    def "correct answer returns positive feedback"() {
        given: 'some quiz'
        Quiz quiz = quizService.createQuiz("quiz1", "question1", List.of("a", "b", "c"), List.of(1))
        and: 'id of quiz '
        def quizId = quiz.id

        when: 'an answer is given to solve the quiz'
        def answer = quizService.solveQuiz(quizId, [1])

        then: 'a positive feedback is given'
        answer == new Answer(true, "Congratulations, you're right!")
    }

    def "wrong answer returns negative feedback"() {
        given: 'a quiz'
        def quiz = quizService.createQuiz("quiz3", "question3", List.of("a", "b", "c"), List.of(3, 1))
        and: 'id of quiz in collection'
        def quizId = quiz.id

        when: 'an answer is given to solve the quiz'
        def answer = quizService.solveQuiz(quizId, List.of(3))

        then: 'a positive feedback is given'
        answer == new Answer(false, "Wrong answer! Please, try again.")

    }

}
