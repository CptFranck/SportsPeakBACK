package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceLogRepository extends CrudRepository<PerformanceLogEntity, Long> {
}
