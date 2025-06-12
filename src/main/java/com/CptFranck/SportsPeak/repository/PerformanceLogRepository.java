package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceLogRepository extends JpaRepository<PerformanceLogEntity, Long> {
    List<PerformanceLogEntity> findAllByTargetSetId(Long targetSetId);
}
