package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
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
                new HashSet<MuscleEntity>(),
                new HashSet<ExerciseTypeEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static ExerciseEntity createNewTestExercise() {
        return new ExerciseEntity(
                null,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<MuscleEntity>(),
                new HashSet<ExerciseTypeEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static ExerciseDto createTestExerciseDto() {
        return new ExerciseDto(
                3L,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<MuscleDto>(),
                new HashSet<ExerciseTypeDto>(),
                new HashSet<ProgExerciseDto>()
        );
    }
}
