package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;

public interface TargetSetManager {

    PerformanceLogEntity savePerformanceLog(PerformanceLogEntity targetExerciseSet);

    void deleteTargetSet(Long id);

}
