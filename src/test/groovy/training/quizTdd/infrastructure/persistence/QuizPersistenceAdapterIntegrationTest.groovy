package training.quizTdd.infrastructure.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class QuizPersistenceAdapterIntegrationTest extends Specification {

    @Autowired
    QuizPersistenceAdapter adapter

    def "should store quiz"() {
        when: 'quiz is stored'
        def result = adapter.createQuiz("quiz1", "question1", List.of("a", "b", "c"), List.of(1))

        then: 'quiz with set id is returned from database'
        result.id != null
        result.id.getClass() == UUID
        result.title == 'quiz1'
    }

    def "should get all stored quizzes from db"() {
        when: 'stored quizzes are retrieved'
        def result = adapter.getQuizzes()

        then: 'all stored quizzes are returned'
        result.size() >= 0
    }


    def "should get a stored quiz by its id"() {
        given: 'a stored quiz id'
        def id = UUID.fromString('000a34c2-58fe-4922-9e3e-fe1f6de35c32')

        when: 'quiz is requested by id'
        def result = adapter.getQuizById(id)

        then: 'matching quiz is returned'
        result != null
        result.id == id
        result.title.contains('quiz')
    }

    def "should return an exception when requested id does not exist"() {
        given: 'some quiz id'
        def id = UUID.randomUUID()

        when: 'quiz is requested by id'
        adapter.getQuizById(id)

        then: 'matching quiz is returned'
        thrown(NoSuchElementException)

    }


}
