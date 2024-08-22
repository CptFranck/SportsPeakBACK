package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;

import java.util.HashSet;

public class TestDataExerciseTypeUtils {

    public static ExerciseTypeEntity createTestExerciseType() {
        return new ExerciseTypeEntity(
                1L,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<ExerciseEntity>()
        );
    }

    public static ExerciseTypeDto createTestExerciseTypeDto() {
        return new ExerciseTypeDto(
                1L,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<ExerciseDto>()
        );
    }
}
