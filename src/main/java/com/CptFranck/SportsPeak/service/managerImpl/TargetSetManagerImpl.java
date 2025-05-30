package com.CptFranck.SportsPeak.service.managerImpl;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetManager;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TargetSetManagerImpl implements TargetSetManager {

    private final TargetSetService targetSetService;

    private final PerformanceLogService performanceLogService;

    public TargetSetManagerImpl(TargetSetService targetSetService, PerformanceLogService performanceLogService) {
        this.targetSetService = targetSetService;
        this.performanceLogService = performanceLogService;
    }

    @Override
    public PerformanceLogEntity savePerformanceLog(PerformanceLogEntity performanceLog) {
        boolean isNew = performanceLog.getId() == null;
        PerformanceLogEntity performanceLogSaved = performanceLogService.save(performanceLog);
        if (isNew) {
            TargetSetEntity targetSet = performanceLogSaved.getTargetSet();
            targetSet.getPerformanceLogs().add(performanceLogSaved);
            targetSetService.save(targetSet);
        }
        return performanceLogSaved;
    }

    @Override
    public void deleteTargetSet(Long id) {
        List<PerformanceLogEntity> performanceLogs = performanceLogService.findAllByTargetSetId(id);
        performanceLogs.forEach(performanceLog -> performanceLogService.delete(performanceLog.getId()));
        targetSetService.delete(id);
    }
}
