package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.PerformanceLogController;
import com.CptFranck.SportsPeak.domain.dto.*;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSetDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceLogControllerTest {

    @InjectMocks
    private PerformanceLogController performanceLogController;

    @Mock
    private Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    @Mock
    private PerformanceLogService performanceLogService;

    @Mock
    private TargetSetService targetSetService;

    private PerformanceLogEntity performanceLog;
    private PerformanceLogDto performanceLogDto;
    private TargetSetEntity targetSet;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        UserDto userDto = createTestUserDto(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        ProgExerciseDto progExerciseDto = createTestProgExerciseDto(1L, userDto, exerciseDto);
        targetSet = createTestTargetSet(1L, progExercise, null);
        TargetSetDto targetSetDto = createTestTargetSetDto(1L, progExerciseDto, null);
        performanceLog = createTestPerformanceLog(1L, targetSet);
        performanceLogDto = createTestPerformanceLogDto(1L, targetSetDto);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogs_Success() {
        when(performanceLogService.findAll()).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        List<PerformanceLogDto> performanceLogDtos = performanceLogController.getPerformanceLogs();

        Assertions.assertEquals(List.of(this.performanceLogDto), performanceLogDtos);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Unsuccessful() {
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.getPerformanceLogById(1L)
        );
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Success() {
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        PerformanceLogDto performanceLogDto = performanceLogController.getPerformanceLogById(1L);

        Assertions.assertEquals(this.performanceLogDto, performanceLogDto);

    }

    @Test
    void PerformanceLogController_GetPerformanceLogsByTargetId_Success() {
        when(performanceLogService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        List<PerformanceLogDto> performanceLogDtos = performanceLogController.getPerformanceLogsByTargetSetsId(1L);

        Assertions.assertEquals(List.of(this.performanceLogDto), performanceLogDtos);
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulTargetSetNotFound() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(
                        createTestInputNewPerformanceLog(1L, false))
        );
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulWrongLabel() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(
                        createTestInputNewPerformanceLog(1L, true))
        );
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_Success() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        PerformanceLogDto performanceLogDto = performanceLogController.addPerformanceLog(
                createTestInputNewPerformanceLog(1L, false)
        );

        Assertions.assertEquals(this.performanceLogDto, performanceLogDto);
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulDoesNotExist() {
        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(
                        createTestInputPerformanceLog(1L, 1L, false)
                )
        );
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulPerformanceNotFound() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(
                        createTestInputPerformanceLog(1L, 1L, true)
                )
        );
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulWrongLabel() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(
                        createTestInputPerformanceLog(1L, 1L, true)
                )
        );
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_Success() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        PerformanceLogDto performanceLogDto = performanceLogController.modifyPerformanceLog(
                createTestInputPerformanceLog(1L, 1L, false)
        );

        Assertions.assertEquals(this.performanceLogDto, performanceLogDto);
    }

    @Test
    void PerformanceLogController_DeletePerformanceLog_Success() {
        Long id = performanceLogController.deletePerformanceLog(1L);

        Assertions.assertEquals(1L, id);
    }
}