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
                    List<Integer> answer);

    List<Quiz> getQuizzes();

    Quiz getQuiz(long id);

    Answer solveQuiz(long id,
                     List<Integer> answers);
}
