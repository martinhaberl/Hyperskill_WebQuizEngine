package training.quizTdd.appcore.domainservices;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;

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
    public Quiz getQuiz(Integer id) {
        Integer index = getIndex(id);
        return quizzes.get(index);
    }

    @Override
    public Answer solveQuiz(Integer quizId, Integer optionNumber) {
        Integer quizIndex = getIndex(quizId);
        Quiz quiz = quizzes.get(quizIndex);

        return quiz.getAnswer() == optionNumber ?
                new Answer(true, "Congratulations, you're right!") :
                new Answer(false, "Wrong answer! Please, try again.");
    }

    private Integer getIndex(Integer id) {
        Integer index = -1;
        for (Quiz quiz : quizzes) {
            if (Objects.equals(quiz.getId(), id)) {
                index = quizzes.indexOf(quiz);
            }
        }

        return index;
    }
}
