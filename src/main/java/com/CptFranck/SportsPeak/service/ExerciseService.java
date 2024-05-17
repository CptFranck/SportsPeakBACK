package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    List<ExerciseEntity> findAll();

    Optional<ExerciseEntity> findOne(Long id);

    boolean exists(Long id);

    ExerciseEntity save(ExerciseEntity exercise);

    void delete(Long id);
}