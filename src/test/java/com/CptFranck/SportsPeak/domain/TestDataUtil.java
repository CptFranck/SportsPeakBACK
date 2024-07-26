package com.CptFranck.SportsPeak.domain;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.Visibility;

import java.util.HashSet;

public class TestDataUtil {
    public static ExerciseTypeEntity createTestExerciseType() {
        return new ExerciseTypeEntity(
                1L,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<ExerciseEntity>()
        );
    }

    public static MuscleEntity createTestMuscle() {
        return new MuscleEntity(
                1L,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseEntity>()
        );
    }

    public static ExerciseEntity createTestExercise() {
        return new ExerciseEntity(
                1L,
                "Exercise name",
                "Exercise description",
                "Exercise goal",
                new HashSet<ExerciseTypeEntity>(),
                new HashSet<MuscleEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static UserEntity createTestUser() {
        return new UserEntity(
                1L,
                "test@test.test",
                "John",
                "Doe",
                "John_Doe",
                "password",
                new HashSet<RoleEntity>(),
                new HashSet<ProgExerciseEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static ProgExerciseEntity createTestProgExercise(UserEntity creator, ExerciseEntity exercise) {
        return new ProgExerciseEntity(
                1L,
                "Exercise name",
                "Exercise note",
                Visibility.PRIVATE,
                TrustLabel.UNVERIFIED,
                new HashSet<UserEntity>(),
                creator,
                exercise,
                new HashSet<TargetSetEntity>()
        );
    }
}
