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

public class TestProgExerciseUtils {

    public static ProgExerciseEntity createTestProgExercise(Long id, UserEntity creator, ExerciseEntity exercise) {
        return new ProgExerciseEntity(
                id,
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

    public static List<ProgExerciseEntity> createNewTestProgExerciseList(UserEntity creator, ExerciseEntity exercise) {
        return List.of(
                createTestProgExercise(null, creator, exercise),
                createTestProgExercise(null, creator, exercise)
        );
    }

    public static ProgExerciseDto createTestProgExerciseDto(Long id, UserDto creator, ExerciseDto exercise) {
        return new ProgExerciseDto(
                id,
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
