package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TargetSetService {

    List<TargetSetEntity> findAll();

    Optional<TargetSetEntity> findOne(Long id);

    Set<TargetSetEntity> findMany(Set<Long> ids);

    void updatePerformanceLogRelation(Set<Long> newIds, Set<Long> oldIds, PerformanceLogEntity performanceLog);

    boolean exists(Long id);

    TargetSetEntity save(TargetSetEntity targetExerciseSet);

    void delete(Long id);
}