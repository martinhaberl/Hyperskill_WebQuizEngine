package training.quizTdd.infrastructure.persistence.entities


import spock.lang.Specification

class QuizEntityTest extends Specification {

    def 'should create a quiz entity'() {
        given: 'parameter values for instance'
        def title = 'The Java Logo'
        def text = 'What is depicted on the Java logo?'
        def options = ['Robot', 'Tea leaf', 'Cup of coffee', 'Bug']
        def answer = [0, 2]

        when: 'instance is created'
        def instance = new QuizEntity(title, text, options, answer)

        then: 'instance attributes are equal to given parameter values'
        instance.getTitle() == title
        instance.getText() == text
        instance.getOptions() == options
        instance.answers[1] == answer[1]

    }
}
