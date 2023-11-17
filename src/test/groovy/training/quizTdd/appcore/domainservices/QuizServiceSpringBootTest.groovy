package training.quizTdd.appcore.domainservices

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import training.quizTdd.appcore.domainmodel.Quiz

@SpringBootTest
class QuizServiceSpringBootTest extends Specification {

    @Autowired
    IQuizService quizService

    @Autowired
    IQuizRepository iQuizRepository

    def "should return valid quiz"() {
        when: 'a quiz is created'
        def quiz = quizService.createQuiz(title, question, options, answer)

        then: 'a quiz instance is returned'
        quiz != null
        quiz.id.getClass() == UUID
        quiz.title.contains('quizTitle')
        quiz.text.contains('quizQuestion')
        quiz.options[0].contains('option')
        quiz.answer[0].getClass() == Integer

        where:
        title        | question        | options                                       | answer
        'quizTitle1' | 'quizQuestion1' | ['option1', 'option2', 'option3']             | [0]
        'quizTitle2' | 'quizQuestion2' | ['option1', 'option2', 'option3', 'options4'] | [1]
        'quizTitle3' | 'quizQuestion3' | ['option1', 'option2']                        | [0, 1]
        'quizTitle4' | 'quizQuestion4' | ['option1', 'option2', 'option3', 'option4']  | [1, 0, 3]
    }

    def "should return all stored quizzes"() {
        when: 'quizzes are retrieved from database'
        def result = quizService.getQuizzes()

        then: 'a list of quizzes is returned'
        result.getClass() == ArrayList<Quiz>
    }

    def "should return quiz by its id"() {
        given: 'UUID of a stored quiz'
        def id = UUID.fromString('000a34c2-58fe-4922-9e3e-fe1f6de35c32')
        when: 'quiz is retrieved from database'
        def result = quizService.getQuiz(id)

        then: 'a list of quizzes is returned'
        result.getClass() == Quiz
        result.id == id
    }

    def "giving correct answer returns positive feedback"() {
        given: 'UUID of a stored quiz'
        def id = UUID.fromString('000a34c2-58fe-4922-9e3e-fe1f6de35c32')
        and: 'correct answer'
        def correctAnswer = [1]

        when: 'answer is checked'
        def result = quizService.solveQuiz(id, correctAnswer)

        then: 'positive feedback is returned'
        result.success() == true
        result.feedback().contains('Congratulations')
    }


    def "giving wrong answer returns negative feedback"() {
        given: 'UUID of a stored quiz'
        def id = UUID.fromString('000a34c2-58fe-4922-9e3e-fe1f6de35c32')
        and: 'wrong answer'
        def correctAnswer = [0]

        when: 'answer is checked'
        def result = quizService.solveQuiz(id, correctAnswer)

        then: 'negative feedback is returned'
        result.success() == false
        result.feedback().contains('Wrong answer')

    }

}
