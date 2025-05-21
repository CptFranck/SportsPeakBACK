package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.resolver.ExerciseTypeInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestInputExerciseType;
import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestInputNewExerciseType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseTypeInputResolverTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseTypeInputResolver exerciseTypeInputResolver;

    @Test
    void resolveInput_ValidInputNewExerciseType_ReturnExerciseTypeEntity() {
        InputNewExerciseType inputNewExercise = createTestInputNewExerciseType();

        when(exerciseService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        ExerciseTypeEntity exerciseTypeResolved = exerciseTypeInputResolver.resolveInput(inputNewExercise);

        assertExerciseTypeInputAndEntity(inputNewExercise, exerciseTypeResolved);
    }

    @Test
    void resolveInput_ValidInputExerciseType_ReturnExerciseTypeEntity() {
        InputExerciseType inputExerciseType = createTestInputExerciseType(1L);

        when(exerciseService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        ExerciseTypeEntity exerciseTypeResolved = exerciseTypeInputResolver.resolveInput(inputExerciseType);

        Assertions.assertEquals(inputExerciseType.getId(), exerciseTypeResolved.getId());
        assertExerciseTypeInputAndEntity(inputExerciseType, exerciseTypeResolved);
    }

    private void assertExerciseTypeInputAndEntity(InputNewExerciseType expectedExerciseType, ExerciseTypeEntity actualExerciseType) {
        Assertions.assertEquals(expectedExerciseType.getName(), actualExerciseType.getName());
        Assertions.assertEquals(expectedExerciseType.getGoal(), actualExerciseType.getGoal());
        Assertions.assertEquals(expectedExerciseType.getExerciseIds().size(), actualExerciseType.getExercises().size());
    }
}
