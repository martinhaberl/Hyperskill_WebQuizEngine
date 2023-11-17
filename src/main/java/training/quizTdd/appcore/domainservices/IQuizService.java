package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Component;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.List;
import java.util.UUID;

@Component
public interface IQuizService {

    Quiz createQuiz(String title,
                    String text,
                    List<String> options,
                    List<Integer> answer);

    List<Quiz> getQuizzes();

    Quiz getQuiz(UUID id);

    Answer solveQuiz(UUID id,
                     List<Integer> answers);
}
