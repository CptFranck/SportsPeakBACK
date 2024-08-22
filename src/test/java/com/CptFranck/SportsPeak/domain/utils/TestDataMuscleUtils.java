package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.HashSet;

public class TestDataMuscleUtils {

    public static MuscleEntity createTestMuscle() {
        return new MuscleEntity(
                2L,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseEntity>()
        );
    }

    public static MuscleDto createTestMuscleDto() {
        return new MuscleDto(
                2L,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseDto>()
        );
    }
}
