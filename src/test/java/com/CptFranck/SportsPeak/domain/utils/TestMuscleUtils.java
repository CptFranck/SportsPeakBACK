package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.HashSet;
import java.util.List;

public class TestMuscleUtils {

    public static MuscleEntity createTestMuscle(Long id) {
        return new MuscleEntity(
                id,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseEntity>()
        );
    }

    public static List<MuscleEntity> createTestMuscleList() {
        return List.of(createTestMuscle(1L), createTestMuscle(2L), createTestMuscle(3L));
    }

    public static MuscleDto createTestMuscleDto(Long id) {
        return new MuscleDto(
                id,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseDto>()
        );
    }
}
