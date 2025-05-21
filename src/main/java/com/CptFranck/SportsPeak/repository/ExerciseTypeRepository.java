package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseTypeRepository extends CrudRepository<ExerciseTypeEntity, Long> {
}
