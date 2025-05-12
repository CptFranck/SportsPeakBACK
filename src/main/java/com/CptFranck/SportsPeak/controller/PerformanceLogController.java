package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.PerformanceLogInputResolver;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetManager;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class PerformanceLogController {

    private final TargetSetManager targetSetManager;

    private final PerformanceLogService performanceLogService;

    private final PerformanceLogInputResolver performanceLogInputResolver;

    private final Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    public PerformanceLogController(TargetSetManager targetSetManager, PerformanceLogService performanceLogService, PerformanceLogInputResolver performanceLogInputResolver, Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper) {
        this.targetSetManager = targetSetManager;
        this.performanceLogMapper = performanceLogMapper;
        this.performanceLogService = performanceLogService;
        this.performanceLogInputResolver = performanceLogInputResolver;
    }

    @DgsQuery
    public List<PerformanceLogDto> getPerformanceLogs() {
        return performanceLogService.findAll().stream().map(performanceLogMapper::mapTo).toList();
    }

    @DgsQuery
    public PerformanceLogDto getPerformanceLogById(@InputArgument Long id) {
        PerformanceLogEntity performanceLog = performanceLogService.findOne(id);
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
        PerformanceLogEntity performanceLog = performanceLogInputResolver.resolveInput(inputNewPerformanceLog);
        return performanceLogMapper.mapTo(targetSetManager.savePerformanceLog(performanceLog));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public PerformanceLogDto modifyPerformanceLog(@InputArgument InputPerformanceLog inputPerformanceLog) {
        PerformanceLogEntity performanceLog = performanceLogInputResolver.resolveInput(inputPerformanceLog);
        return performanceLogMapper.mapTo(performanceLogService.save(performanceLog));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deletePerformanceLog(@InputArgument Long performanceLogId) {
        performanceLogService.delete(performanceLogId);
        return performanceLogId;
    }
}
