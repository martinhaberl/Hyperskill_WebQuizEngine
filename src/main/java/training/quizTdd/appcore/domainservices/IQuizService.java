package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Component;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.List;
import java.util.Optional;

@Component
public interface IQuizService {

    Quiz createQuiz(String title,
                    String text,
                    List<String> options,
                    List<Integer> answer);

    List<Quiz> getQuizzes();

    Optional<Quiz> getQuiz(Integer id);

    Optional<Answer> solveQuiz(Integer id, Integer optionNumber);
}
