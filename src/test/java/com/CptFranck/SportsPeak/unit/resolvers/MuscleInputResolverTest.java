package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.resolver.MuscleInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MuscleInputResolverTest {

    @Mock
    private MuscleService muscleService;

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private MuscleInputResolver muscleInputResolver;

    @Test
    void resolveInput_ValidInputNewMuscle_ReturnMuscleEntity() {
        InputNewMuscle inputNewMuscle = createTestInputNewMuscle();
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        MuscleEntity muscleResolved = muscleInputResolver.resolveInput(inputNewMuscle);

        assertMuscleInputAndEntity(inputNewMuscle, muscleResolved);
    }

    @Test
    void resolveInput_ValidInputMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = createTestMuscle(1L);
        InputMuscle inputMuscle = createTestInputMuscle(1L);
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(muscle);

        MuscleEntity muscleResolved = muscleInputResolver.resolveInput(inputMuscle);

        Assertions.assertEquals(inputMuscle.getId(), muscleResolved.getId());
        assertMuscleInputAndEntity(inputMuscle, muscleResolved);
    }

    private void assertMuscleInputAndEntity(InputNewMuscle expectedMuscle, MuscleEntity actualMuscle) {
        Assertions.assertEquals(expectedMuscle.getName(), actualMuscle.getName());
        Assertions.assertEquals(expectedMuscle.getDescription(), actualMuscle.getDescription());
        Assertions.assertEquals(expectedMuscle.getFunction(), actualMuscle.getFunction());
        Assertions.assertEquals(expectedMuscle.getExerciseIds().size(), actualMuscle.getExercises().size());
    }
}
