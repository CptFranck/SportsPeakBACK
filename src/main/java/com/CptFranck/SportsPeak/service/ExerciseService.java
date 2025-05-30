package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.List;
import java.util.Set;

public interface ExerciseService {

    List<ExerciseEntity> findAll();

    ExerciseEntity findOne(Long id);

    Set<ExerciseEntity> findMany(Set<Long> ids);

    ExerciseEntity save(ExerciseEntity exercise);

    void delete(Long id);

    void updateExerciseTypeRelation(Set<Long> newIds, Set<Long> oldIds, ExerciseTypeEntity exerciseType);

    void updateMuscleRelation(Set<Long> newIds, Set<Long> oldIds, MuscleEntity muscle);

    boolean exists(Long id);
}