package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class QuizService implements IQuizService {

    List<Quiz> quizzes = new ArrayList<>();

    public QuizService() {
    }

    @Override
    public Quiz createQuiz(String title, String text, List<String> options, Integer answer) {
        Quiz quiz = new Quiz(title, text, options, answer);
        quizzes.add(quiz);
        return quiz;
    }

    @Override
    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    @Override
    public Quiz getQuiz(Integer quizId) {
        Integer quizIndex = getIndex(quizId);
        return quizzes.get(quizIndex);
    }

    @Override
    public Answer solveQuiz(Integer quizId, Integer optionNumber) {
        Integer quizIndex = getIndex(quizId);
        Quiz quiz = quizzes.get(quizIndex);

        return quiz.getAnswer() == optionNumber ?
                new Answer(true, "Congratulations, you're right!") :
                new Answer(false, "Wrong answer! Please, try again.");
    }

    public Integer getIndex(Integer quizId) {
        Integer index = null;

        for (Quiz quiz : quizzes) {
            if (Objects.equals(quiz.getId(), quizId)) {
                index = quizzes.indexOf(quiz);
            }
        }

        if (index == null) {
            throw new QuizNotFoundException("Quiz with id %d does not exist.".formatted(quizId));
        }

        return index;
    }
}
