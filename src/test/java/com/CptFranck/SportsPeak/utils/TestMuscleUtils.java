package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestMuscleUtils {

    public static MuscleEntity createTestMuscle(Long id) {
        return new MuscleEntity(
                id,
                "Muscle name",
                "Muscle latin name",
                "Muscle description",
                "Muscle function",
                "Muscle illustration path",
                new HashSet<>()
        );
    }

    public static List<MuscleEntity> createTestMuscleList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestMuscle(null), createTestMuscle(null), createTestMuscle(null));
        else
            return List.of(createTestMuscle(1L), createTestMuscle(2L), createTestMuscle(3L));
    }

    public static MuscleDto createTestMuscleDto(Long id) {
        return new MuscleDto(
                id,
                "Muscle name",
                "Muscle latin name",
                "Muscle description",
                "Muscle function",
                "Muscle illustration path",
                new HashSet<>()
        );
    }

    public static InputNewMuscle createTestInputNewMuscle() {
        return new InputNewMuscle(
                "name",
                "latinName",
                "function",
                "description",
                new ArrayList<>()
        );
    }

    public static InputMuscle createTestInputMuscle(Long id) {
        return new InputMuscle(
                id,
                "name",
                "latinName",
                "function",
                "description",
                new ArrayList<>()
        );
    }

    public static void assertEqualMuscle(MuscleEntity expected, MuscleEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getLatinName(), actual.getLatinName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getFunction(), actual.getFunction());
        Assertions.assertEquals(expected.getExercises().size(), actual.getExercises().size());
    }

    public static void assertMuscleDtoAndEntity(MuscleEntity muscleEntity, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(muscleEntity.getId(), muscleDto.getId());
        Assertions.assertEquals(muscleEntity.getName(), muscleDto.getName());
        Assertions.assertEquals(muscleEntity.getLatinName(), muscleDto.getLatinName());
        Assertions.assertEquals(muscleEntity.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(muscleEntity.getDescription(), muscleDto.getDescription());
        Assertions.assertEquals(muscleEntity.getExercises().size(), muscleDto.getExercises().size());
    }

    public static void assertMuscleDtoAndInput(InputNewMuscle inputNewMuscle, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(inputNewMuscle.getName(), muscleDto.getName());
        Assertions.assertEquals(inputNewMuscle.getLatinName(), muscleDto.getLatinName());
        Assertions.assertEquals(inputNewMuscle.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(inputNewMuscle.getDescription(), muscleDto.getDescription());
        Assertions.assertEquals(inputNewMuscle.getExerciseIds().size(), muscleDto.getExercises().size());
    }
}
