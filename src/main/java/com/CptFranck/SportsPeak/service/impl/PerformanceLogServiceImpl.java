package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repositories.PerformanceLogRepository;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PerformanceLogServiceImpl implements PerformanceLogService {

    private final TargetSetService targetSetService;

    private final PerformanceLogRepository performanceLogRepository;

    public PerformanceLogServiceImpl(TargetSetService targetSetService, PerformanceLogRepository performanceLogRepository) {
        this.targetSetService = targetSetService;
        this.performanceLogRepository = performanceLogRepository;
    }

    @Override
    public List<PerformanceLogEntity> findAll() {
        return StreamSupport.stream(performanceLogRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public PerformanceLogEntity findOne(Long id) {
        return performanceLogRepository.findById(id).orElseThrow(() -> new PerformanceLogNotFoundException(id));
    }

    @Override
    public Set<PerformanceLogEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(performanceLogRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public List<PerformanceLogEntity> findAllByTargetSetId(Long id) {
        return performanceLogRepository.findAllByTargetSetId(id);
    }

    @Override
    public PerformanceLogEntity save(PerformanceLogEntity performanceLog) {
        if (performanceLog.getId() == null) {
            PerformanceLogEntity performanceLogSaved = performanceLogRepository.save(performanceLog);
            TargetSetEntity targetSet = performanceLogSaved.getTargetSet();
            targetSet.getPerformanceLogs().add(performanceLogSaved);
            targetSetService.save(targetSet);
            return performanceLogSaved;
        }
        if (exists(performanceLog.getId()))
            return performanceLogRepository.save(performanceLog);
        throw new PerformanceLogNotFoundException(performanceLog.getId());
    }

    @Override
    public void delete(Long id) {
        PerformanceLogEntity performanceLog = this.findOne(id);
        performanceLogRepository.delete(performanceLog);
    }

    @Override
    public boolean exists(Long id) {
        return performanceLogRepository.existsById(id);
    }
}
