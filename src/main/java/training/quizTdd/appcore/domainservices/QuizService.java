package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class QuizService implements IQuizService {

    private final IQuizRepository repository;

    public QuizService(IQuizRepository repository) {
        this.repository = repository;
    }

    @Override
    public Quiz createQuiz(final String title,
                           final String text,
                           final List<String> options,
                           final List<Integer> answer) {

        return repository.createQuiz(title, text, options, answer);
    }

    @Override
    public List<Quiz> getQuizzes() {
        return repository.getQuizzes();
    }

    @Override
    public Quiz getQuiz(long quizId) {
        return repository.getQuizById(quizId);
    }

    @Override
    public Answer solveQuiz(long quizId, List<Integer> givenAnswers) {

        Quiz quiz = getQuiz(quizId);

        ArrayList<Integer> sortableGivenAnswers = new ArrayList<>(givenAnswers);
        ArrayList<Integer> sortableStoredAnswers = new ArrayList<>(quiz.getAnswer());

        if (quiz.getAnswer().size() != givenAnswers.size()) {
            return getNegativeAnswer();
        }

        if (givenAnswers.size() > 1) {
            Collections.sort(sortableGivenAnswers);
            Collections.sort(sortableStoredAnswers);
        }

        if (Objects.equals(sortableGivenAnswers, sortableStoredAnswers)) {
            return getPositiveAnswer();
        } else {
            return getNegativeAnswer();
        }

    }

    private Answer getPositiveAnswer() {
        return new Answer(true, "Congratulations, you're right!");
    }

    private Answer getNegativeAnswer() {
        return new Answer(false, "Wrong answer! Please, try again.");
    }
}
