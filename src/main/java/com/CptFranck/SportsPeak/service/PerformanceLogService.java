package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PerformanceLogService {

    List<PerformanceLogEntity> findAll();

    Optional<PerformanceLogEntity> findOne(Long id);

    Set<PerformanceLogEntity> findMany(Set<Long> ids);

    List<PerformanceLogEntity> findAllByTargetSetId(Long id);

    boolean exists(Long id);

    PerformanceLogEntity save(PerformanceLogEntity targetExerciseSet);

    void delete(Long id);
}