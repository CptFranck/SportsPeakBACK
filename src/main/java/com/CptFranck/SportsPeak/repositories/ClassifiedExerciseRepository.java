package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.compositeKey.ClassifiedExerciseKey;
import org.springframework.data.repository.CrudRepository;

public interface ClassifiedExerciseRepository extends CrudRepository<MuscleEntity, ClassifiedExerciseKey> {
}
