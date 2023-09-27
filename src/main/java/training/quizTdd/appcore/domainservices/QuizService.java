package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.ArrayList;
import java.util.Collections;
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

        if (quizOptional.isEmpty()) {
            return Optional.of(getQuizDoesNotExistAnswer());
        }

        if (quizOptional.isPresent()) {

            ArrayList<Integer> sortableGivenAnswers = new ArrayList<>(givenAnswers);
            ArrayList<Integer> sortableStoredAnwers = new ArrayList<>(quizOptional.get().getAnswer());

            if (quizOptional.get().getAnswer().size() != givenAnswers.size()) {
                return Optional.of(getNegativeAnswer());
            }


            if (givenAnswers.size() > 1) {
                Collections.sort(sortableGivenAnswers);
                Collections.sort(sortableStoredAnwers);
            }

            if (Objects.equals(sortableGivenAnswers, sortableStoredAnwers)) {
                return Optional.of(getPositiveAnswer());
            } else {
                return Optional.of(getNegativeAnswer());
            }
        }
        return Optional.of(new Answer(false, "den Fall habe ich wohl noch  nicht betrachtet"));
    }

    private Answer getPositiveAnswer() {
        return new Answer(true, "Congratulations, you're right!");
    }

    private Answer getNegativeAnswer() {
        return new Answer(false, "Wrong answer! Please, try again.");
    }

    private Answer getQuizDoesNotExistAnswer() {
        return new Answer(false, "Quiz does not exist.");
    }
}
