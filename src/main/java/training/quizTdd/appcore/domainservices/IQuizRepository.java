package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Component;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.List;

@Component
public interface IQuizRepository {
    Quiz createQuiz(String title, String text, List<String> options, List<Integer> answers);

    List<Quiz> getQuizzes();

    Quiz getQuizById(long id);

}
