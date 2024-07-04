package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetExerciseSetEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TargetExerciseSetService {

    List<TargetExerciseSetEntity> findAll();

    Optional<TargetExerciseSetEntity> findOne(Long id);

    Set<TargetExerciseSetEntity> findMany(Set<Long> ids);

    void updatePerformanceLogRelation(Set<Long> newIds, Set<Long> oldIds, PerformanceLogEntity performanceLog);

    boolean exists(Long id);

    TargetExerciseSetEntity save(TargetExerciseSetEntity targetExerciseSet);

    void delete(Long id);
}