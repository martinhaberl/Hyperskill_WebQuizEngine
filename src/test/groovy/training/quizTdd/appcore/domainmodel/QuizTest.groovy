package training.quizTdd.appcore.domainmodel

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class QuizTest extends Specification {

    def 'should return Quiz instance'() {
        given: 'parameter values for instance'
        def title = 'The Java Logo'
        def text = 'What is depicted on the Java logo?'
        def options = ['Robot', 'Tea leaf', 'Cup of coffee', 'Bug']
        def answer = [0, 2]

        when: 'instance is created'
        def instance = new Quiz(title, text, options, answer)

        then: 'instance attributes are equal to given parameter values'
        instance.getTitle() == title
        instance.getText() == text
        instance.getOptions() == options
        instance.getAnswer() == answer
        instance.getAnswer()[1] == answer[1]
        and: 'quiz got an id'
        instance.getId() != null
        instance.getId() > 0
    }

}