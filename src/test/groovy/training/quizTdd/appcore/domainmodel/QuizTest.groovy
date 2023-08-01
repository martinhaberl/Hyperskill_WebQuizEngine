package training.quizTdd.appcore.domainmodel

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class QuizTest extends Specification {

    def 'should return title of Quiz instance'() {
        given: 'parameter values for instance'
        def title = 'The Java Logo'
        def text = 'What is depicted on the Java logo?'
        def options = ['Robot', 'Tea leaf', 'Cup of coffee', 'Bug']
        def answer = 2

        when: 'instance is created'
        def instance = new Quiz(title, text, options, answer)

        then: 'instance attributes are equal to given parameter values'
        instance.getTitle() == title
        instance.getText() == text
        instance.getOptions() == options
        instance.getAnswer() == answer
        and: 'quiz got an unique id'
        instance.getId() != null
    }
}