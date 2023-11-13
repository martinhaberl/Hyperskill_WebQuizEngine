package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Component;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.infrastructure.persistence.entities.QuizEntity;

import java.util.UUID;

@Component
public interface IQuizRepository {
    Quiz createQuiz(Quiz quiz);

    Iterable<QuizEntity> getQuizzes();

    Quiz getQuizById(UUID id);

}
