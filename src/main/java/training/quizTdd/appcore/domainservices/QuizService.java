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
    public Optional<Quiz> getQuiz(Integer quizId) {
        return quizzes.stream().filter(quiz -> Objects.equals(quiz.getId(), quizId)).findFirst();
    }

    @Override
    public Optional<Answer> solveQuiz(Integer quizId, Integer optionNumber) {

        Optional<Quiz> quizOptional = getQuiz(quizId);

        if (quizOptional.isPresent()) {
            return Optional.of(quizOptional.get().getAnswer() == optionNumber ?
                    new Answer(true, "Congratulations, you're right!") :
                    new Answer(false, "Wrong answer! Please, try again."));
        } else {
            return Optional.of(new Answer(false, "-1"));
        }
    }

    public Integer getIndex(Integer quizId) {
        Integer index = null;

        for (Quiz quiz : quizzes) {
            if (Objects.equals(quiz.getId(), quizId)) {
                index = quizzes.indexOf(quiz);
            }
        }

        return index;
    }

}
