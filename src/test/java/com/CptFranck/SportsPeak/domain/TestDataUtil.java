package com.CptFranck.SportsPeak.domain;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.Visibility;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;

import java.time.Duration;
import java.time.LocalDateTime;
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
                2L,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseEntity>()
        );
    }

    public static ExerciseEntity createTestExercise() {
        return new ExerciseEntity(
                3L,
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
                4L,
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
                5L,
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

    public static TargetSetEntity createTestTargetSet(ProgExerciseEntity progExercise) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetEntity(
                6L,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME,
                effortTime,
                restTime,
                creationDate,
                TargetSetState.USED,
                progExercise,
                null,
                new HashSet<PerformanceLogEntity>()
        );
    }

    public static TargetSetEntity createTestTargetSetUpdate(ProgExerciseEntity progExercise, TargetSetEntity update) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetEntity(
                6L,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME,
                effortTime,
                restTime,
                creationDate,
                TargetSetState.USED,
                progExercise,
                update,
                new HashSet<PerformanceLogEntity>()
        );
    }

    public static PerformanceLogEntity createTestPerformanceLog(TargetSetEntity targetSet) {
        LocalDateTime creationDate = LocalDateTime.now();
        return new PerformanceLogEntity(
                7L,
                1,
                5,
                0f,
                WeightUnit.KILOGRAMME,
                creationDate,
                targetSet
        );
    }
}
