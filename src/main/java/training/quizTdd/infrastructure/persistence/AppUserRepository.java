package training.quizTdd.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;
import training.quizTdd.infrastructure.persistence.entities.AppUserEntity;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUserEntity, Integer> {
    Optional<AppUserEntity> findAppUserByUsername(String username);
}
