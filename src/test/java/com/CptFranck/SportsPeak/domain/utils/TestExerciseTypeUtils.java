package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;

import java.util.ArrayList;
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
                new HashSet<ExerciseDto>()
        );
    }


    public static InputNewExerciseType createTestInputNewExerciseType() {
        return new InputNewExerciseType(
                "name",
                "goal",
                new ArrayList<Long>()
        );
    }

    public static InputExerciseType createTestInputExerciseType() {
        return new InputExerciseType(
                1L,
                "name",
                "goal",
                new ArrayList<Long>()
        );
    }
}
