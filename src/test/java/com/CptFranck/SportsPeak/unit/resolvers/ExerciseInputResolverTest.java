package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.resolvers.ExerciseInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseInputResolverTest {

    @Mock
    private MuscleService muscleService;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private ExerciseTypeService exerciseTypeService;

    @InjectMocks
    private ExerciseInputResolver exerciseInputResolver;

    @Test
    void resolveInput_ValidInputNewExercise_ReturnExerciseEntity() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        when(muscleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        ExerciseEntity exerciseResolved = exerciseInputResolver.resolveInput(inputNewExercise);

        assertExerciseInputAndEntity(inputNewExercise, exerciseResolved);
    }

    @Test
    void resolveInput_ValidInputExercise_ReturnExerciseEntity() {
        ExerciseEntity exercise = createTestExercise(1L);
        InputExercise inputExercise = createTestInputExercise(1L);
        when(muscleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(exercise);

        ExerciseEntity exerciseResolved = exerciseInputResolver.resolveInput(inputExercise);

        Assertions.assertEquals(inputExercise.getId(), exerciseResolved.getId());
        assertExerciseInputAndEntity(inputExercise, exerciseResolved);
        Assertions.assertEquals(exercise.getProgExercises().size(), exerciseResolved.getProgExercises().size());
    }

    private void assertExerciseInputAndEntity(InputNewExercise expectedExercise, ExerciseEntity actualExercise) {
        Assertions.assertEquals(expectedExercise.getName(), actualExercise.getName());
        Assertions.assertEquals(expectedExercise.getDescription(), actualExercise.getDescription());
        Assertions.assertEquals(expectedExercise.getGoal(), actualExercise.getGoal());
        Assertions.assertEquals(expectedExercise.getMuscleIds().size(), actualExercise.getMuscles().size());
        Assertions.assertEquals(expectedExercise.getExerciseTypeIds().size(), actualExercise.getExerciseTypes().size());
    }
}
