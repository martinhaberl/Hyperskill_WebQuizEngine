package training.quizTdd.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import training.quizTdd.infrastructure.persistence.entities.QuizEntity;

import java.util.UUID;

@Repository
public interface QuizCrudRepository extends CrudRepository<QuizEntity, UUID> {

}