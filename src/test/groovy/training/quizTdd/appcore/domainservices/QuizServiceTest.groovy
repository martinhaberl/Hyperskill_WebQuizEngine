package training.quizTdd.appcore.domainservices

import spock.lang.Specification
import spock.lang.Unroll
import training.quizTdd.appcore.domainmodel.Answer

class QuizServiceTest extends Specification {

    @Unroll
    def 'should create a Quiz and store it'() {
        given: 'a QuizService'
        IQuizService quizService = new QuizService()

        when: 'a quiz is created'
        def quiz = quizService.createQuiz(title, question, options, answer)

        then: 'a quiz instance is returned'
        quiz.getTitle() == title
        quiz.getText() == question
        quiz.getOptions() == options
        quiz.getAnswer() == answer
        and: 'Quiz is stored in Collection'
        quizService.getQuizzes().size() == 1
        quizService.getQuizzes().get(0).getTitle() == title
        quizService.getQuizzes().get(0).getOptions().size() == options.size()
        quizService.getQuizzes().get(0).getAnswer().size() == answer.size()

        where:
        title        | question        | options                                       | answer
        'quizTitle1' | 'quizQuestion1' | ['option1', 'option2', 'option3']             | [0]
        'quizTitle2' | 'quizQuestion2' | ['option1', 'option2', 'option3', 'options4'] | [1]
        'quizTitle3' | 'quizQuestion3' | ['option1', 'option2']                        | [0, 1]
        'quizTitle4' | 'quizQuestion4' | ['option1', 'option2', 'option3', 'option4']  | [1, 0, 3]

    }

    def "should return all stored quizzes"() {
        given: 'a quiz service'
        IQuizService quizService = new QuizService()
        and: '2 quizzes'
        quizService.createQuiz('title1', 'question', ['option'], [0])
        quizService.createQuiz('title2', 'question2', ['option1'], [2])

        when: 'stored quizzes are requested'
        def quizzes = quizService.getQuizzes()

        then: 'all quizzes are returned'
        quizzes.size() == 2
        quizzes.get(0).getId() != null
        quizzes.get(1).getAnswer() == [2]
    }

    def 'should return specific quiz by id'() {
        given: 'a quiz service'
        IQuizService quizService = new QuizService()
        and: 'a quiz'
        quizService.createQuiz('title1', 'question', ['option'], [0])
        and: 'a generated quizId'
        def quizId = quizService.getQuizzes().get(0).getId()

        when:
        def specificQuiz = quizService.getQuiz(quizId)

        then:
        specificQuiz.get().id == quizId
        specificQuiz.get().options.size() == quizService.getQuizzes().get(0).getOptions().size()

    }


    def "correct answer returns positive feedback"() {
        given: 'a quiz service instance'
        IQuizService quizService = new QuizService()
        and: 'some quizzes'
        quizService.createQuiz("quiz1", "question1", List.of("a", "b", "c"), List.of(1))
        quizService.createQuiz("quiz2", "question2", List.of("a", "b", "c"), List.of(0, 2))
        quizService.createQuiz("quiz3", "question3", List.of("a", "b", "c"), List.of(3, 1))
        and: 'id of first quiz in collection'
        def quizId = quizService.getQuizzes().get(quizIndex).getId()

        when: 'an answer is given to solve the quiz'
        def answer = quizService.solveQuiz(quizId, answerGiven)

        then: 'a positive feedback is given'
        answer.get() == new Answer(true, "Congratulations, you're right!")

        where:
        quizIndex | answerGiven
        0         | [1]
        1         | [0, 2]
        1         | [2]
        2         | [3]
        2         | [1]
    }

    def "wrong answer returns negative feedback"() {
        given: 'a quiz service instance'
        IQuizService quizService = new QuizService()
        and: 'some quizzes'
        quizService.createQuiz("quiz1", "question1", List.of("a", "b", "c"), List.of(1))
        quizService.createQuiz("quiz2", "question2", List.of("a", "b", "c"), List.of(0, 2))
        quizService.createQuiz("quiz3", "question3", List.of("a", "b", "c"), List.of(3, 1))
        and: 'id of first quiz in collection'
        def quizId = quizService.getQuizzes().get(quizIndex).getId()

        when: 'an answer is given to solve the quiz'
        def answer = quizService.solveQuiz(quizId, answerGiven)

        then: 'a positive feedback is given'
        answer.get() == new Answer(false, "Wrong answer! Please, try again.")

        where:
        quizIndex | answerGiven
        0         | [2]
        1         | [11156]
        2         | [-234213]
    }

}
