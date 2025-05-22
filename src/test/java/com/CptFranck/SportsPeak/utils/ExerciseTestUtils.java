package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExerciseTestUtils {

    public static ExerciseEntity createTestExercise(Long id) {
        return new ExerciseEntity(
                id,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static List<ExerciseEntity> createTestExerciseList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestExercise(null), createTestExercise(null), createTestExercise(null));
        else
            return List.of(createTestExercise(1L), createTestExercise(2L), createTestExercise(3L));
    }

    public static ExerciseDto createTestExerciseDto(Long id) {
        return new ExerciseDto(
                id,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static InputNewExercise createTestInputNewExercise() {
        return new InputNewExercise(
                "name",
                "description",
                "goal",
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static InputExercise createTestInputExercise(Long id) {
        return new InputExercise(
                id,
                "name",
                "description",
                "goal",
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static void assertEqualExercise(ExerciseEntity expected, ExerciseEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getGoal(), actual.getGoal());
        Assertions.assertEquals(expected.getMuscles().size(), actual.getMuscles().size());
        Assertions.assertEquals(expected.getExerciseTypes().size(), actual.getExerciseTypes().size());
        Assertions.assertEquals(expected.getProgExercises().size(), actual.getProgExercises().size());
    }

    public static void assertExerciseDtoAndEntity(ExerciseEntity exerciseEntity, ExerciseDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(exerciseEntity.getId(), exerciseDto.getId());
        Assertions.assertEquals(exerciseEntity.getName(), exerciseDto.getName());
        Assertions.assertEquals(exerciseEntity.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(exerciseEntity.getDescription(), exerciseDto.getDescription());
        Assertions.assertEquals(exerciseEntity.getMuscles().size(), exerciseDto.getMuscles().size());
        Assertions.assertEquals(exerciseEntity.getExerciseTypes().size(), exerciseDto.getProgExercises().size());
        Assertions.assertEquals(exerciseEntity.getProgExercises().size(), exerciseDto.getProgExercises().size());
    }

    public static void assertExerciseDtoAndInput(InputNewExercise inputNewExercise, ExerciseDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(inputNewExercise.getName(), exerciseDto.getName());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(inputNewExercise.getDescription(), exerciseDto.getDescription());
        Assertions.assertEquals(inputNewExercise.getMuscleIds().size(), exerciseDto.getMuscles().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseDto.getProgExercises().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseDto.getProgExercises().size());
    }
}
