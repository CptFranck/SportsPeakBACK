package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;

import java.util.List;
import java.util.Set;

public interface ExerciseTypeService {

    List<ExerciseTypeEntity> findAll();

    ExerciseTypeEntity findOne(Long id);

    Set<ExerciseTypeEntity> findMany(Set<Long> ids);

    ExerciseTypeEntity create(ExerciseTypeEntity newExerciseType);

    ExerciseTypeEntity update(ExerciseTypeEntity updatedExerciseType);

    void delete(Long id);

    boolean exists(Long id);
}