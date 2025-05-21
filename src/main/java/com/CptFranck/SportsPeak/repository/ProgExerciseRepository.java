package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgExerciseRepository extends CrudRepository<ProgExerciseEntity, Long> {
    List<ProgExerciseEntity> findAllBySubscribedUsersId(Long id);
}
