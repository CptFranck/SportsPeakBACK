package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestExerciseUtils {

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
}
