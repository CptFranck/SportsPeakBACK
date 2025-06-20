package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repository.PerformanceLogRepository;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PerformanceLogServiceImpl implements PerformanceLogService {


    private final PerformanceLogRepository performanceLogRepository;

    public PerformanceLogServiceImpl(PerformanceLogRepository performanceLogRepository) {
        this.performanceLogRepository = performanceLogRepository;
    }

    @Override
    public List<PerformanceLogEntity> findAll() {
        return new ArrayList<>(performanceLogRepository.findAll());
    }

    @Override
    public PerformanceLogEntity findOne(Long id) {
        return performanceLogRepository.findById(id).orElseThrow(() -> new PerformanceLogNotFoundException(id));
    }

    @Override
    public Set<PerformanceLogEntity> findMany(Set<Long> ids) {
        return new HashSet<>(performanceLogRepository.findAllById(ids));
    }

    @Override
    public List<PerformanceLogEntity> findAllByTargetSetId(Long id) {
        return performanceLogRepository.findAllByTargetSetId(id);
    }

    @Override
    public PerformanceLogEntity save(PerformanceLogEntity performanceLog) {
        if (performanceLog.getId() == null)
            return performanceLogRepository.save(performanceLog);
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
