package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;

import java.util.HashSet;
import java.util.List;

public class TestExerciseTypeUtils {

    public static ExerciseTypeEntity createTestExerciseType(Long id) {
        return new ExerciseTypeEntity(
                id,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<ExerciseEntity>()
        );
    }

    public static List<ExerciseTypeEntity> createTestExerciseTypeList() {
        return List.of(createTestExerciseType(1L), createTestExerciseType(2L), createTestExerciseType(3L));
    }

    public static ExerciseTypeDto createTestExerciseTypeDto(Long id) {
        return new ExerciseTypeDto(
                id,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<ExerciseDto>()
        );
    }
}
