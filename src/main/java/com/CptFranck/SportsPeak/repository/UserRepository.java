package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String login);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAllBySubscribedProgExercisesContaining(ProgExerciseEntity progExercise);
}
