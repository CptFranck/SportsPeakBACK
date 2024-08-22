package com.CptFranck.SportsPeak.domain;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;

import java.util.HashSet;

public class TestDataExerciseUtils {

    public static ExerciseEntity createTestExercise() {
        return new ExerciseEntity(
                3L,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<ExerciseTypeEntity>(),
                new HashSet<MuscleEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static ExerciseEntity createNewTestExercise() {
        return new ExerciseEntity(
                null,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<ExerciseTypeEntity>(),
                new HashSet<MuscleEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }
}
