package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.resolvers.ExerciseInputResolver;
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
    private ExerciseTypeService exerciseTypeService;

    @InjectMocks
    private ExerciseInputResolver exerciseInputResolver;

    @Test
    void resolveInput_ValidInputNewExercise_ReturnExerciseEntity() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        when(muscleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        ExerciseEntity exerciseSaved = exerciseInputResolver.resolveInput(inputNewExercise);

        Assertions.assertEquals(inputNewExercise.getName(), exerciseSaved.getName());
        Assertions.assertEquals(inputNewExercise.getDescription(), exerciseSaved.getDescription());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseSaved.getGoal());
        Assertions.assertEquals(inputNewExercise.getMuscleIds().size(), exerciseSaved.getMuscles().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseSaved.getExerciseTypes().size());
    }

    @Test
    void resolveInput_ValidInputExercise_ReturnExerciseEntity() {
        ExerciseEntity exercise = createTestExercise(1L);
        InputExercise inputMuscle = createTestInputExercise(1L);
        when(muscleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        ExerciseEntity exerciseSaved = exerciseInputResolver.resolveInput(inputMuscle, exercise);

        Assertions.assertEquals(inputMuscle.getId(), exerciseSaved.getId());
        Assertions.assertEquals(inputMuscle.getName(), exerciseSaved.getName());
        Assertions.assertEquals(inputMuscle.getDescription(), exerciseSaved.getDescription());
        Assertions.assertEquals(inputMuscle.getGoal(), exerciseSaved.getGoal());
        Assertions.assertEquals(inputMuscle.getMuscleIds().size(), exerciseSaved.getMuscles().size());
        Assertions.assertEquals(inputMuscle.getExerciseTypeIds().size(), exerciseSaved.getExerciseTypes().size());
        Assertions.assertEquals(exercise.getProgExercises().size(), exerciseSaved.getProgExercises().size());
    }
}
