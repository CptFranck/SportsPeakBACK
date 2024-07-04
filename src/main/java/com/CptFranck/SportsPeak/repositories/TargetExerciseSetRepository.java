package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.TargetExerciseSetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetExerciseSetRepository extends CrudRepository<TargetExerciseSetEntity, Long> {
}
