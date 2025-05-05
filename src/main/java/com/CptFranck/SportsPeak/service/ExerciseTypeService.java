package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;

import java.util.List;
import java.util.Set;

public interface ExerciseTypeService {

    List<ExerciseTypeEntity> findAll();

    ExerciseTypeEntity findOne(Long id);

    Set<ExerciseTypeEntity> findMany(Set<Long> ids);

    ExerciseTypeEntity create(InputNewExerciseType inputNewExercise);

    ExerciseTypeEntity update(InputExerciseType inputExercise);

    ExerciseTypeEntity save(ExerciseTypeEntity exercise);

    void delete(Long id);

    boolean exists(Long id);
}