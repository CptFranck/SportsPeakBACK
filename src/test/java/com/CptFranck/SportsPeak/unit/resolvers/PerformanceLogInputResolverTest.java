package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.resolvers.PerformanceLogInputResolver;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createTestInputNewPerformanceLog;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createTestInputPerformanceLog;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceLogInputResolverTest {

    @InjectMocks
    private PerformanceLogInputResolver performanceLogInputResolver;

    @Mock
    private TargetSetService targetSetService;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void resolveInput_ValidInputNewMuscle_ReturnMuscleEntity() {
        InputNewPerformanceLog newPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), false);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);

        PerformanceLogEntity performanceLogSaved = performanceLogInputResolver.resolveInput(newPerformanceLog);

        assertPerformanceLogInputAndEntity(newPerformanceLog, performanceLogSaved);
    }

    @Test
    void resolveInput_ValidInputMuscle_ReturnMuscleEntity() {
        InputPerformanceLog newPerformanceLog = createTestInputPerformanceLog(1L, targetSet.getId(), false);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);

        PerformanceLogEntity performanceLogSaved = performanceLogInputResolver.resolveInput(newPerformanceLog);

        Assertions.assertEquals(newPerformanceLog.getId(), performanceLogSaved.getId());
        assertPerformanceLogInputAndEntity(newPerformanceLog, performanceLogSaved);
    }

    private void assertPerformanceLogInputAndEntity(InputNewPerformanceLog expectedPerformanceLog, PerformanceLogEntity actualPerformanceLog) {
        Assertions.assertEquals(expectedPerformanceLog.getSetIndex(), actualPerformanceLog.getSetIndex());
        Assertions.assertEquals(expectedPerformanceLog.getRepetitionNumber(), actualPerformanceLog.getRepetitionNumber());
        Assertions.assertEquals(expectedPerformanceLog.getWeight(), actualPerformanceLog.getWeight());
        Assertions.assertEquals(expectedPerformanceLog.getWeightUnit(), actualPerformanceLog.getWeightUnit().label);
        Assertions.assertEquals(expectedPerformanceLog.getLogDate(), actualPerformanceLog.getLogDate());
        Assertions.assertEquals(expectedPerformanceLog.getTargetSetId(), actualPerformanceLog.getTargetSet().getId());
    }
}
