package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Component;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.List;

@Component
public interface IQuizService {

    Quiz createQuiz(String title,
                    String text,
                    List<String> options,
                    Integer answer);

    List<Quiz> getQuizzes();

    Quiz getQuiz(Integer id);

    Answer solveQuiz(Integer id, Integer optionNumber);
}
