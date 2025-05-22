package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExerciseTypeTestUtils {

    public static ExerciseTypeEntity createTestExerciseType(Long id) {
        return new ExerciseTypeEntity(
                id,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<>()
        );
    }

    public static List<ExerciseTypeEntity> createTestExerciseTypeList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestExerciseType(null), createTestExerciseType(null), createTestExerciseType(null));
        else
            return List.of(createTestExerciseType(1L), createTestExerciseType(2L), createTestExerciseType(3L));
    }

    public static ExerciseTypeDto createTestExerciseTypeDto(Long id) {
        return new ExerciseTypeDto(
                id,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<>()
        );
    }


    public static InputNewExerciseType createTestInputNewExerciseType() {
        return new InputNewExerciseType(
                "name",
                "goal",
                new ArrayList<>()
        );
    }

    public static InputExerciseType createTestInputExerciseType(Long id) {
        return new InputExerciseType(
                id,
                "name",
                "goal",
                new ArrayList<>()
        );
    }

    public static void asserEqualExerciseType(ExerciseTypeEntity exerciseTypeExpected, ExerciseTypeEntity exerciseTypeObtain) {
        Assertions.assertEquals(exerciseTypeExpected.getId(), exerciseTypeObtain.getId());
        Assertions.assertEquals(exerciseTypeExpected.getName(), exerciseTypeObtain.getName());
        Assertions.assertEquals(exerciseTypeExpected.getGoal(), exerciseTypeObtain.getGoal());
        Assertions.assertEquals(exerciseTypeExpected.getExercises().size(), exerciseTypeObtain.getExercises().size());
    }

    public static void assertExerciseTypeDtoAndEntity(ExerciseTypeEntity exerciseTypeEntity, ExerciseTypeDto exerciseTypeDto) {
        Assertions.assertNotNull(exerciseTypeDto);
        Assertions.assertEquals(exerciseTypeEntity.getId(), exerciseTypeDto.getId());
        Assertions.assertEquals(exerciseTypeEntity.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(exerciseTypeEntity.getGoal(), exerciseTypeDto.getGoal());
        Assertions.assertEquals(exerciseTypeEntity.getExercises().size(), exerciseTypeDto.getExercises().size());
    }

    public static void assertExerciseTypeDtoAndInput(InputNewExerciseType inputNewExercise, ExerciseTypeDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(inputNewExercise.getName(), exerciseDto.getName());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(inputNewExercise.getExerciseIds().size(), exerciseDto.getExercises().size());
    }
}
