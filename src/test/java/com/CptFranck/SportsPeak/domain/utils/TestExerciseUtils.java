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
import java.util.List;

public class TestExerciseUtils {

    public static ExerciseEntity createTestExercise(Long id) {
        return new ExerciseEntity(
                id,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<MuscleEntity>(),
                new HashSet<ExerciseTypeEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static List<ExerciseEntity> createTestExerciseList() {
        return List.of(createTestExercise(1L), createTestExercise(2L), createTestExercise(3L));
    }

    public static ExerciseDto createTestExerciseDto(Long id) {
        return new ExerciseDto(
                id,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<MuscleDto>(),
                new HashSet<ExerciseTypeDto>(),
                new HashSet<ProgExerciseDto>()
        );
    }
}
