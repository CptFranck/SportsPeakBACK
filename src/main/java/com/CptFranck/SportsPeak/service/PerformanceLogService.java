package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;

import java.util.List;
import java.util.Set;

public interface PerformanceLogService {

    List<PerformanceLogEntity> findAll();

    PerformanceLogEntity findOne(Long id);

    Set<PerformanceLogEntity> findMany(Set<Long> ids);

    List<PerformanceLogEntity> findAllByTargetSetId(Long id);

    PerformanceLogEntity save(PerformanceLogEntity performanceLog);

    void delete(Long id);

    boolean exists(Long id);
}