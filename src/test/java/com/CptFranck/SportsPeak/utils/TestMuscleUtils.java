package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestMuscleUtils {

    public static MuscleEntity createTestMuscle(Long id) {
        return new MuscleEntity(
                id,
                "Muscle name",
                "Muscle latin name",
                "Muscle description",
                "Muscle function",
                "Muscle illustration path",
                new HashSet<ExerciseEntity>()
        );
    }

    public static List<MuscleEntity> createTestMuscleList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestMuscle(null), createTestMuscle(null), createTestMuscle(null));
        else
            return List.of(createTestMuscle(1L), createTestMuscle(2L), createTestMuscle(3L));
    }

    public static MuscleDto createTestMuscleDto(Long id) {
        return new MuscleDto(
                id,
                "Muscle name",
                "Muscle latin name",
                "Muscle description",
                "Muscle function",
                "Muscle illustration path",
                new HashSet<ExerciseDto>()
        );
    }

    public static InputNewMuscle createTestInputNewMuscle() {
        return new InputNewMuscle(
                "name",
                "latinName",
                "function",
                "description",
                new ArrayList<Long>()
        );
    }

    public static InputMuscle createTestInputMuscle(Long id) {
        return new InputMuscle(
                id,
                "name",
                "latinName",
                "function",
                "description",
                new ArrayList<Long>()
        );
    }
}
