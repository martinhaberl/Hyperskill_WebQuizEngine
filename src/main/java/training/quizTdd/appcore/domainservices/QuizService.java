package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuizService implements IQuizService {

    private final List<Quiz> quizzes = new ArrayList<>();

    public QuizService() {
    }

    @Override
    public Quiz createQuiz(String title, String text, List<String> options, List<Integer> answer) {
        Quiz quiz = new Quiz(title, text, options, answer);
        quizzes.add(quiz);
        return quiz;
    }

    @Override
    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    @Override
    public Optional<Quiz> getQuiz(Integer quizId) {
        return quizzes.stream().filter(quiz -> Objects.equals(quiz.getId(), quizId)).findFirst();
    }

    @Override
    public Optional<Answer> solveQuiz(Integer quizId, List<Integer> givenAnswers) {

        Optional<Quiz> quizOptional = getQuiz(quizId);

        if (quizOptional.isPresent() && givenAnswers != null) {
            boolean storedAnswersAndGivenAnswersAreBothCorrectlyEmpty =
                    quizOptional.get().getAnswer().size() == 0 && givenAnswers.size() == 0;

            boolean storedAnswersContainGivenAnswers =
                    givenAnswers.stream().anyMatch(integer ->
                            quizOptional.get()
                                    .getAnswer()
                                    .contains(integer)
                    );

            return Optional.of(storedAnswersAndGivenAnswersAreBothCorrectlyEmpty || storedAnswersContainGivenAnswers ?
                    new Answer(true, "Congratulations, you're right!") :
                    new Answer(false, "Wrong answer! Please, try again."));
        } else {
            return Optional.of(new Answer(false, "-1"));
        }
    }

}
