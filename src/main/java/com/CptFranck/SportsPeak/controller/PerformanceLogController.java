package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class PerformanceLogController {

    private final TargetSetService targetSetService;
    private final PerformanceLogService performanceLogService;
    private final Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    public PerformanceLogController(TargetSetService targetSetService, TargetSetService targetSetService1, PerformanceLogService performanceLogService, Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper) {
        this.targetSetService = targetSetService;
        this.performanceLogMapper = performanceLogMapper;
        this.performanceLogService = performanceLogService;
    }

    @DgsQuery
    public List<PerformanceLogDto> getPerformanceLogs() {
        return performanceLogService.findAll().stream().map(performanceLogMapper::mapTo).toList();
    }

    @DgsQuery
    public PerformanceLogDto getPerformanceLogById(@InputArgument Long id) {
        Optional<PerformanceLogEntity> targetSet = performanceLogService.findOne(id);
        return targetSet.map(performanceLogMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public PerformanceLogDto addPerformanceLog(@InputArgument InputNewPerformanceLog inputNewPerformanceLog) {
        return performanceLogMapper.mapTo(inputToEntity(inputNewPerformanceLog));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public PerformanceLogDto modifyPerformanceLog(@InputArgument InputPerformanceLog inputPerformanceLog) {
        if (!performanceLogService.exists(inputPerformanceLog.getId())) {
            return null;
        }
        return performanceLogMapper.mapTo(inputToEntity(inputPerformanceLog));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deletePerformanceLog(@InputArgument Long targetSetId) {
        if (!performanceLogService.exists(targetSetId)) {
            return null;
        }
        performanceLogService.delete(targetSetId);
        return targetSetId;
    }

    private PerformanceLogEntity inputToEntity(InputNewPerformanceLog inputNewPerformanceLog) {
        TargetSetEntity targetSet = targetSetService.findOne(inputNewPerformanceLog.getTargetSetId()).orElseThrow(
                () -> new TargetSetNotFoundException(inputNewPerformanceLog.getTargetSetId()));

        Long id = null;
        if (inputNewPerformanceLog instanceof InputPerformanceLog) {
            id = ((InputPerformanceLog) inputNewPerformanceLog).getId();
        }

        PerformanceLogEntity performanceLogEntity = new PerformanceLogEntity(
                id,
                inputNewPerformanceLog.getSetIndex(),
                inputNewPerformanceLog.getRepetitionNumber(),
                inputNewPerformanceLog.getWeight(),
                inputNewPerformanceLog.getWeightUnit(),
                inputNewPerformanceLog.getLogDate(),
                targetSet
        );

        performanceLogEntity = performanceLogService.save(performanceLogEntity);
        if (id == null) {
            targetSet.getPerformanceLogs().add(performanceLogEntity);
            targetSetService.save(targetSet);
        }
        return performanceLogEntity;
    }
}
