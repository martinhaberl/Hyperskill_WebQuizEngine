package training.quizTdd.appcore.domainservices

import spock.lang.Specification
import training.quizTdd.appcore.domainmodel.Answer

class QuizServiceTest extends Specification {


    def "correct answer returns positive feedback"() {
        given:
        'a quiz service instance'
        IQuizService quizService = new QuizService()
        and: 'some quizzes'
        quizService.createQuiz("quiz1", "question1", List.of("a", "b", "c"), 1)
        quizService.createQuiz("quiz2", "question2", List.of("a", "b", "c"), 2)
        quizService.createQuiz("quiz3", "question3", List.of("a", "b", "c"), 3)
        and: 'id of third quiz in collection'
        def quizId = quizService.getQuizzes().get(2).getId()

        when: 'an answer is given to solve the quiz'
        def answer = quizService.solveQuiz(quizId, 3)

        then: 'a positive feedback is given'
        answer.get() == new Answer(true, "Congratulations, you're right!")
    }

    def "wrong answer returns negative feedback"() {
        given:
        'a quiz service instance'
        IQuizService quizService = new QuizService()
        and: 'some quizzes'
        quizService.createQuiz("quiz1", "question1", List.of("a", "b", "c"), 1)
        quizService.createQuiz("quiz2", "question2", List.of("a", "b", "c"), 2)
        quizService.createQuiz("quiz3", "question3", List.of("a", "b", "c"), 3)
        and: 'id of first quiz in collection'
        def quizId = quizService.getQuizzes().get(0).getId()

        when: 'an answer is given to solve the quiz'
        def answer = quizService.solveQuiz(quizId, 3)

        then: 'a positive feedback is given'
        answer.get() == new Answer(false, "Wrong answer! Please, try again.")
    }
}
