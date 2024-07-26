package com.CptFranck.SportsPeak.domain;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.HashSet;

public class TestDataUtil {
    public static ExerciseTypeEntity createTestExerciseType() {
        return new ExerciseTypeEntity(
                1L,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<>()
        );
    }

    public static ExerciseEntity createTestExercise() {
        return new ExerciseEntity(
                1L,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static MuscleEntity createTestMuscle() {
        return new MuscleEntity(
                1L,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<>()
        );
    }
}
