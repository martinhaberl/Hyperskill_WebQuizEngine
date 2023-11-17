package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Component;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.List;
import java.util.UUID;

@Component
public interface IQuizRepository {
    Quiz createQuiz(String title, String text, List<String> options, List<Integer> answers);

    List<Quiz> getQuizzes();

    Quiz getQuizById(UUID id);

}
