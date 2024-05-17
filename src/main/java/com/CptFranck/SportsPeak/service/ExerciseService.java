package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExerciseService {

    List<ExerciseEntity> findAll();

    Optional<ExerciseEntity> findOne(Long id);

    Set<ExerciseEntity> findMany(Set<Long> ids);

    boolean exists(Long id);

    ExerciseEntity save(ExerciseEntity exercise);

    void delete(Long id);
}