package training.quizTdd.infrastructure.persistence;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.appcore.domainservices.IQuizRepository;
import training.quizTdd.infrastructure.persistence.entities.QuizEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QuizPersistenceAdapter implements IQuizRepository {

    private final QuizCrudRepository quizCrudRepository;

    public QuizPersistenceAdapter(QuizCrudRepository quizCrudRepository) {
        this.quizCrudRepository = quizCrudRepository;
    }

    private static QuizEntity map(final String title, final String text, final List<String> options, final List<Integer> answers) {
        return new QuizEntity(title, text, options, answers);
    }

    private static Quiz map(final QuizEntity savedEntity) {
        return new Quiz(savedEntity.getId(), savedEntity.getTitle(), savedEntity.getText(), savedEntity.getOptions(), savedEntity.getAnswers());
    }

    @Override
    public Quiz createQuiz(String title, String text, List<String> options, List<Integer> answers) {
        QuizEntity entity = map(title, text, options, answers);
        final QuizEntity storedEntity = quizCrudRepository.save(entity);

        return map(storedEntity);
    }

    @Override
    public List<Quiz> getQuizzes() {
        final Iterable<QuizEntity> storedEntities = quizCrudRepository.findAll();
        final List<Quiz> quizzes = new ArrayList<>();

        for (QuizEntity entity : storedEntities) {
            quizzes.add(map(entity));
        }

        return quizzes;
    }

    @Override
    public Quiz getQuizById(final long id) {
        final Optional<QuizEntity> storedEntityOptional = quizCrudRepository.findById(id);

        return storedEntityOptional.map(QuizPersistenceAdapter::map).orElseThrow(NoSuchElementException::new);
    }
}

