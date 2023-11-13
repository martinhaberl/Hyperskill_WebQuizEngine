package training.quizTdd.infrastructure.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import training.quizTdd.infrastructure.persistence.entities.QuizEntity

@SpringBootTest
class QuizRepositoryTest extends Specification {

    @Autowired
    QuizRepository quizRepository

    def "new quiz should be stored"() {
        given: 'a valid quiz entity'
        def entity = new QuizEntity(UUID.randomUUID(),
                'someEntityTitle',
                'someEntityText',
                List.of('someEntityOption1', 'someEntityOption2'),
                List.of(0))

        when: 'quiz entity is stored'
        def result = quizRepository.save(entity)

        then: 'id is set'
        result.id != ""
    }
}
