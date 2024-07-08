package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProgExerciseService {

    List<ProgExerciseEntity> findAll();

    Optional<ProgExerciseEntity> findOne(Long id);

    Set<ProgExerciseEntity> findMany(Set<Long> ids);

    List<ProgExerciseEntity> findAllByUserId(Long userId);

    boolean exists(Long id);

    ProgExerciseEntity save(ProgExerciseEntity exercise);

    void delete(Long id);
}