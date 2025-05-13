package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;

import java.util.List;
import java.util.Set;

public interface ProgExerciseService {

    List<ProgExerciseEntity> findAll();

    ProgExerciseEntity findOne(Long id);

    Set<ProgExerciseEntity> findMany(Set<Long> ids);

    ProgExerciseEntity save(ProgExerciseEntity exercise);

    void delete(Long id);

    boolean exists(Long id);
}