package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repositories.PerformanceLogRepository;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PerformanceLogServiceImpl implements PerformanceLogService {

    PerformanceLogRepository performanceLogRepository;

    public PerformanceLogServiceImpl(PerformanceLogRepository performanceLogRepository) {
        this.performanceLogRepository = performanceLogRepository;
    }

    @Override
    public PerformanceLogEntity save(PerformanceLogEntity exerciseTypeEntity) {
        return performanceLogRepository.save(exerciseTypeEntity);
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
    public Optional<PerformanceLogEntity> findOne(Long id) {
        return performanceLogRepository.findById(id);
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
    public boolean exists(Long id) {
        return performanceLogRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        PerformanceLogEntity performanceLog = performanceLogRepository.findById(id).orElseThrow(() -> new PerformanceLogNotFoundException(id));
        performanceLogRepository.delete(performanceLog);
    }
}
