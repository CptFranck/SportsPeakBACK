package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.PerformanceLogController;
import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.PerformanceLogInputResolver;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceLogControllerTest {

    @InjectMocks
    private PerformanceLogController performanceLogController;

    @Mock
    private Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    @Mock
    private PerformanceLogInputResolver performanceLogInputResolver;

    @Mock
    private PerformanceLogService performanceLogService;

    @Mock
    private TargetSetManager targetSetManager;

    private PerformanceLogEntity performanceLog;
    private PerformanceLogDto performanceLogDto;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        performanceLog = createTestPerformanceLog(1L, targetSet);
        performanceLogDto = createTestPerformanceLogDto(1L);
    }

    @Test
    void getPerformanceLogs_ValidUse_ReturnListOfPerformanceLogDto() {
        when(performanceLogService.findAll()).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        List<PerformanceLogDto> performanceLogDtos = performanceLogController.getPerformanceLogs();

        Assertions.assertEquals(List.of(this.performanceLogDto), performanceLogDtos);
    }

    @Test
    void getPerformanceLogById_ValidMuscleId_ReturnPerformanceLogDto() {
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        PerformanceLogDto performanceLogDto = performanceLogController.getPerformanceLogById(1L);

        Assertions.assertEquals(this.performanceLogDto, performanceLogDto);
    }

    @Test
    void getPerformanceLogsByTargetSetsId_ValidInput_ReturnListOfPerformanceLogDto() {
        when(performanceLogService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        List<PerformanceLogDto> performanceLogDtos = performanceLogController.getPerformanceLogsByTargetSetsId(1L);

        Assertions.assertEquals(List.of(this.performanceLogDto), performanceLogDtos);
    }

    @Test
    void addPerformanceLog_ValidInput_ReturnPerformanceLogDto() {
        when(performanceLogInputResolver.resolveInput(Mockito.any(InputNewPerformanceLog.class))).thenReturn(performanceLog);
        when(targetSetManager.savePerformanceLog(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        PerformanceLogDto performanceLogDto = performanceLogController.addPerformanceLog(
                createTestInputNewPerformanceLog(1L, false));

        Assertions.assertEquals(this.performanceLogDto, performanceLogDto);
    }

    @Test
    void modifyPerformanceLog_ValidInput_ReturnPerformanceLogDto() {
        when(performanceLogInputResolver.resolveInput(Mockito.any(InputPerformanceLog.class))).thenReturn(performanceLog);
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        PerformanceLogDto performanceLogDto = performanceLogController.modifyPerformanceLog(
                createTestInputPerformanceLog(1L, 1L, false));

        Assertions.assertEquals(this.performanceLogDto, performanceLogDto);
    }

    @Test
    void deletePerformanceLog_ValidInput_ReturnPerformanceLogId() {
        Long id = performanceLogController.deletePerformanceLog(1L);

        Assertions.assertEquals(1L, id);
    }
}