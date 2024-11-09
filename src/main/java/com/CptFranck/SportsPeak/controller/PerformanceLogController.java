package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
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

@DgsComponent
public class PerformanceLogController {

    private final TargetSetService targetSetService;
    private final PerformanceLogService performanceLogService;
    private final Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    public PerformanceLogController(TargetSetService targetSetService, PerformanceLogService performanceLogService, Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper) {
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
        PerformanceLogEntity performanceLog = performanceLogService.findOne(id)
                .orElseThrow(() -> new PerformanceLogNotFoundException(id));
        return performanceLogMapper.mapTo(performanceLog);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsQuery
    public List<PerformanceLogDto> getPerformanceLogsByTargetSetsId(@InputArgument Long targetSetId) {
        return performanceLogService.findAllByTargetSetId(targetSetId).stream().map(performanceLogMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public PerformanceLogDto addPerformanceLog(@InputArgument InputNewPerformanceLog inputNewPerformanceLog) {
        return performanceLogMapper.mapTo(inputToEntity(inputNewPerformanceLog));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public PerformanceLogDto modifyPerformanceLog(@InputArgument InputPerformanceLog inputPerformanceLog) {
        return performanceLogMapper.mapTo(inputToEntity(inputPerformanceLog));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deletePerformanceLog(@InputArgument Long performanceLogId) {
        performanceLogService.delete(performanceLogId);
        return performanceLogId;
    }

    private PerformanceLogEntity inputToEntity(InputNewPerformanceLog inputNewPerformanceLog) {
        TargetSetEntity targetSet = targetSetService.findOne(inputNewPerformanceLog.getTargetSetId()).orElseThrow(
                () -> new TargetSetNotFoundException(inputNewPerformanceLog.getTargetSetId()));

        Long id;
        if (inputNewPerformanceLog instanceof InputPerformanceLog) {
            id = ((InputPerformanceLog) inputNewPerformanceLog).getId();
            if (!performanceLogService.exists(id))
                throw new PerformanceLogNotFoundException(id);
        } else {
            id = null;
        }

        PerformanceLogEntity performanceLogEntity = new PerformanceLogEntity(
                id,
                inputNewPerformanceLog.getSetIndex(),
                inputNewPerformanceLog.getRepetitionNumber(),
                inputNewPerformanceLog.getWeight(),
                WeightUnit.valueOfLabel(inputNewPerformanceLog.getWeightUnit()),
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
