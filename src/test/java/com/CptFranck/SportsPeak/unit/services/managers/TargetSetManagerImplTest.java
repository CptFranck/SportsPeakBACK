package com.CptFranck.SportsPeak.unit.services.managers;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.CptFranck.SportsPeak.service.managerImpl.TargetSetManagerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TargetSetManagerImplTest {

    @InjectMocks
    private TargetSetManagerImpl targetSetManager;

    @Mock
    private TargetSetService targetSetService;

    @Mock
    private PerformanceLogService performanceLogService;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void savePerformanceLog_AddNewPerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);

        PerformanceLogEntity performanceLogSaved = targetSetManager.savePerformanceLog(performanceLog);

        Assertions.assertEquals(performanceLog, performanceLogSaved);
    }

    @Test
    void savePerformanceLog_UpdatePerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);

        PerformanceLogEntity performanceLogSaved = targetSetManager.savePerformanceLog(performanceLog);

        Assertions.assertEquals(performanceLog, performanceLogSaved);
    }

    @Test
    void deleteTargetSet_ValidUse_Void() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);
        when(performanceLogService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));

        assertAll(() -> targetSetManager.deleteTargetSet(targetSet.getId()));
    }
}
