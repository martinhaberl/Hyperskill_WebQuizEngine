package training.quizTdd.infrastructure.persistence;

import org.springframework.stereotype.Service;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.appcore.domainservices.IQuizRepository;
import training.quizTdd.infrastructure.persistence.entities.QuizEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuizPersistenceAdapter implements IQuizRepository {

    private final QuizRepository quizRepository;

    public QuizPersistenceAdapter(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    private static QuizEntity map(final Quiz quiz) {
        return new QuizEntity(quiz.getTitle(), quiz.getText(), quiz.getOptions(), quiz.getAnswer());
    }

    private static Quiz map(final QuizEntity savedEntity) {
        return new Quiz(savedEntity.getId(), savedEntity.getTitle(), savedEntity.getText(), savedEntity.getOptions(), savedEntity.getAnswers());
    }

    @Override
    public Quiz createQuiz(final Quiz quiz) {
        QuizEntity entity = map(quiz);
        final QuizEntity storedEntity = quizRepository.save(entity);

        return map(storedEntity);
    }

    @Override
    public List<QuizEntity> getQuizzes() {
        final Iterable<QuizEntity> storedEntities = quizRepository.findAll();
        final List<QuizEntity> entities = new ArrayList<QuizEntity>();

        storedEntities.forEach(entities::add);

        return entities;
    }

    @Override
    public Quiz getQuizById(final UUID id) {
        final Optional<QuizEntity> storedEntityOptional = quizRepository.findById(id);

        return storedEntityOptional.map(QuizPersistenceAdapter::map)
                .orElseThrow(NoSuchElementException::new);
    }
}
