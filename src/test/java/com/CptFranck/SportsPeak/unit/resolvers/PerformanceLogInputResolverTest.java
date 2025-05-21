package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.resolver.PerformanceLogInputResolver;
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
    void resolveInput_InvalidNewInputLabel_ThrowLabelMatchNotFoundException() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> performanceLogInputResolver.resolveInput(inputNewPerformanceLog));
    }

    @Test
    void resolveInput_ValidInputNewPerformanceLog_ReturnPerformanceLogEntity() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), false);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);

        PerformanceLogEntity performanceLogResolved = performanceLogInputResolver.resolveInput(inputNewPerformanceLog);

        assertPerformanceLogInputAndEntity(inputNewPerformanceLog, performanceLogResolved);
    }

    @Test
    void resolveInput_InvalidInputLabel_ThrowLabelMatchNotFoundException() {
        InputPerformanceLog inputNewPerformanceLog = createTestInputPerformanceLog(1L, targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> performanceLogInputResolver.resolveInput(inputNewPerformanceLog));
    }

    @Test
    void resolveInput_ValidInputPerformanceLog_ReturnPerformanceLogEntity() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(1L, targetSet.getId(), false);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);

        PerformanceLogEntity performanceLogResolved = performanceLogInputResolver.resolveInput(inputPerformanceLog);

        Assertions.assertEquals(inputPerformanceLog.getId(), performanceLogResolved.getId());
        assertPerformanceLogInputAndEntity(inputPerformanceLog, performanceLogResolved);
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
