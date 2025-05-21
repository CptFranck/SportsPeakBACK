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

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
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

        PerformanceLogEntity performanceLogResolved = targetSetManager.savePerformanceLog(performanceLog);

        Assertions.assertEquals(performanceLog, performanceLogResolved);
    }

    @Test
    void savePerformanceLogUpdatePerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);

        PerformanceLogEntity performanceLogResolved = targetSetManager.savePerformanceLog(performanceLog);

        Assertions.assertEquals(performanceLog, performanceLogResolved);
    }

    @Test
    void deleteTargetSet_ValidUse_Void() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);
        when(performanceLogService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));

        assertAll(() -> targetSetManager.deleteTargetSet(targetSet.getId()));
    }
}
