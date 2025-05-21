package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceLogRepository extends CrudRepository<PerformanceLogEntity, Long> {
    List<PerformanceLogEntity> findAllByTargetSetId(Long targetSetId);
}
