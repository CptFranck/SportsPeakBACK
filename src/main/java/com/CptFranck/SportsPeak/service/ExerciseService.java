package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;

import java.util.List;
import java.util.Set;

public interface ExerciseService {

    List<ExerciseEntity> findAll();

    ExerciseEntity findOne(Long id);

    Set<ExerciseEntity> findMany(Set<Long> ids);

    ExerciseEntity create(InputNewExercise inputNewExercise);

    ExerciseEntity update(InputExercise inputExercise);

    ExerciseEntity save(ExerciseEntity exercise);

    void delete(Long id);

    void updateExerciseTypeRelation(Set<Long> newIds, Set<Long> oldIds, ExerciseTypeEntity exerciseType);

    void updateMuscleRelation(Set<Long> newIds, Set<Long> oldIds, MuscleEntity muscle);

    void updateProgExerciseRelation(ExerciseEntity newExercise, ExerciseEntity oldExercise, ProgExerciseEntity progExercise);

    boolean exists(Long id);
}