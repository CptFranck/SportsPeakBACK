package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProgExerciseService {

    List<ProgExerciseEntity> findAll();

    Optional<ProgExerciseEntity> findOne(Long id);

    Set<ProgExerciseEntity> findMany(Set<Long> ids);

    List<ProgExerciseEntity> findByUserId(Long userId);

    void updateTargetExerciseSetRelation(Set<Long> newIds, Set<Long> oldIds, TargetSetEntity targetSet);

    boolean exists(Long id);

    ProgExerciseEntity save(ProgExerciseEntity exercise);

    void delete(Long id);
}