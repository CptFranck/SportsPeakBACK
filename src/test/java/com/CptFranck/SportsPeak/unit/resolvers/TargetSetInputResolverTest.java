package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.input.targetSet.AbstractTargetSetInput;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.resolver.TargetSetInputResolver;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
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
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TargetSetInputResolverTest {

    @InjectMocks
    private TargetSetInputResolver targetSetInputResolver;

    @Mock
    private TargetSetService targetSetService;

    @Mock
    private ProgExerciseService progExerciseService;

    private ProgExerciseEntity progExercise;
    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void resolveInput_InvalidInputNewLabel_ThrowLabelMatchNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, true);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputNewTargetSet));
    }

    @Test
    void resolveInput_ValidInputNewPerformanceLog_ReturnMuscleEntity() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, false);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);

        TargetSetEntity targetSetResolved = targetSetInputResolver.resolveInput(inputNewTargetSet);

        assertTargetSetInputAndEntity(inputNewTargetSet, targetSetResolved);
    }

    @Test
    void resolveInput_InvalidInputLabel_ThrowLabelMatchNotFoundException() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(progExercise.getId(), true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputTargetSet));
    }

    @Test
    void resolveInput_ValidInputPerformanceLog_ReturnMuscleEntity() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(progExercise.getId(), false);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);

        TargetSetEntity targetSetResolved = targetSetInputResolver.resolveInput(inputTargetSet);

        assertTargetSetInputAndEntity(inputTargetSet, targetSetResolved);
    }

    @Test
    void resolveInput_InvalidInputTargetStateLabel_ThrowLabelMatchNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(1L, true);
        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputTargetSetState));
    }

    @Test
    void resolveInput_ValidInputTargetState_ReturnTargetSetState() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(1L, false);

        TargetSetState targetSetStateResolved = targetSetInputResolver.resolveInput(inputTargetSetState);

        Assertions.assertEquals(inputTargetSetState.getState(), targetSetStateResolved.label);
    }

    private void assertTargetSetInputAndEntity(AbstractTargetSetInput expectedTargetSet, TargetSetEntity actualTargetSet) {
        if (expectedTargetSet instanceof InputTargetSet inputTargetSet)
            Assertions.assertEquals(inputTargetSet.getId(), actualTargetSet.getId());
        if (expectedTargetSet instanceof InputNewTargetSet inputNewTargetSet) {
            Assertions.assertEquals(inputNewTargetSet.getCreationDate(), actualTargetSet.getCreationDate());
            Assertions.assertEquals(inputNewTargetSet.getProgExerciseId(), actualTargetSet.getProgExercise().getId());
            if (inputNewTargetSet.getTargetSetUpdateId() != null)
                Assertions.assertEquals(inputNewTargetSet.getTargetSetUpdateId(), actualTargetSet.getTargetSetUpdate().getId());
        }
        Assertions.assertEquals(expectedTargetSet.getIndex(), actualTargetSet.getIndex());
        Assertions.assertEquals(expectedTargetSet.getSetNumber(), actualTargetSet.getSetNumber());
        Assertions.assertEquals(expectedTargetSet.getRepetitionNumber(), actualTargetSet.getRepetitionNumber());
        Assertions.assertEquals(expectedTargetSet.getWeight(), actualTargetSet.getWeight());
        Assertions.assertEquals(expectedTargetSet.getWeightUnit(), actualTargetSet.getWeightUnit().label);
        Assertions.assertEquals(expectedTargetSet.getPhysicalExertionUnitTime().InputDurationToDuration(),
                actualTargetSet.getPhysicalExertionUnitTime());
        Assertions.assertEquals(expectedTargetSet.getRestTime().InputDurationToDuration(),
                actualTargetSet.getRestTime());
    }
}
