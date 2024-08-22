package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.Visibility;

import java.util.HashSet;
import java.util.List;

public class TestDataProgExerciseUtils {

    public static ProgExerciseEntity createTestProgExercise(UserEntity creator, ExerciseEntity exercise) {
        return new ProgExerciseEntity(
                5L,
                "Prog Exercise name",
                "Prog Exercise note",
                Visibility.PRIVATE,
                TrustLabel.UNVERIFIED,
                new HashSet<UserEntity>(),
                creator,
                exercise,
                new HashSet<TargetSetEntity>()
        );
    }

    public static ProgExerciseEntity createTestNewProgExercise(UserEntity creator, ExerciseEntity exercise) {
        return new ProgExerciseEntity(
                null,
                "Prog Exercise name",
                "Prog Exercise note",
                Visibility.PRIVATE,
                TrustLabel.UNVERIFIED,
                new HashSet<UserEntity>(),
                creator,
                exercise,
                new HashSet<TargetSetEntity>()
        );
    }

    public static List<ProgExerciseEntity> createNewTestProgExercises(UserEntity creator, ExerciseEntity exercise) {
        return List.of(createTestNewProgExercise(creator, exercise), createTestNewProgExercise(creator, exercise));
    }

    public static ProgExerciseDto createTestProgExerciseDto(UserDto creator, ExerciseDto exercise) {
        return new ProgExerciseDto(
                5L,
                "Prog Exercise name",
                "Prog Exercise note",
                Visibility.PRIVATE.label,
                TrustLabel.UNVERIFIED.label,
                creator,
                exercise,
                new HashSet<TargetSetDto>()
        );
    }
}
