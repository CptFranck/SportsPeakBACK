package com.CptFranck.SportsPeak.domain;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.Visibility;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class TestDataUtil {
    public static ExerciseTypeEntity createTestExerciseType() {
        return new ExerciseTypeEntity(
                1L,
                "Exercise type name",
                "Exercise type goal",
                new HashSet<ExerciseEntity>()
        );
    }

    public static ExerciseTypeEntity createNewTestExerciseType() {
        return new ExerciseTypeEntity(
                null,
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

    public static MuscleEntity createNewTestMuscle() {
        return new MuscleEntity(
                null,
                "Muscle name",
                "Muscle description",
                "Muscle function",
                new HashSet<ExerciseEntity>()
        );
    }

    public static List<MuscleEntity> createNewTestMuscles() {
        return List.of(createNewTestMuscle(), createNewTestMuscle());
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

    public static ExerciseEntity createNewTestExercise() {
        return new ExerciseEntity(
                null,
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

    public static UserEntity createNewTestUser(int option) {
        String email = "test@test.test";
        String firstName = "firstName";
        String lastName = "lastName";
        String userName = "userName";
        if (option == 1) {
            email = "john.doe@email.com";
            firstName = "John";
            lastName = "Doe";
            userName = "John_Doe";
        } else if (option == 2) {
            email = "jane.doe@email.com";
            firstName = "Jane";
            lastName = "Doe";
            userName = "Jane_Doe";
        }
        return new UserEntity(
                null,
                email,
                firstName,
                lastName,
                userName,
                "password",
                new HashSet<RoleEntity>(),
                new HashSet<ProgExerciseEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static List<UserEntity> createNewTestUsers() {
        return List.of(createNewTestUser(1), createNewTestUser(2));
    }

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

    public static PrivilegeEntity createTestPrivilege() {
        return new PrivilegeEntity(
                8L,
                "Privilege name",
                new HashSet<RoleEntity>()
        );
    }

    public static PrivilegeEntity createNewTestPrivilege(int option) {
        String privilgeName = "Privilege name";
        if (option == 1) {
            privilgeName = "Privilege One";
        } else if (option == 2) {
            privilgeName = "Privilege Two";
        }
        return new PrivilegeEntity(
                null,
                privilgeName,
                new HashSet<RoleEntity>()
        );
    }

    public static List<PrivilegeEntity> createNewTestPrivileges() {
        return List.of(createNewTestPrivilege(1), createNewTestPrivilege(2));
    }

    public static RoleEntity createTestRole() {
        return new RoleEntity(
                9L,
                "Role name",
                new HashSet<UserEntity>(),
                new HashSet<PrivilegeEntity>()
        );
    }

    public static RoleEntity createNewTestRole(int option) {
        String roleName = "Role name";
        if (option == 1) {
            roleName = "Role One";
        } else if (option == 2) {
            roleName = "Role Two";
        }
        return new RoleEntity(
                null,
                roleName,
                new HashSet<UserEntity>(),
                new HashSet<PrivilegeEntity>()
        );
    }

    public static List<RoleEntity> createNewTestRoles() {
        return List.of(createNewTestRole(1), createNewTestRole(2));
    }
}
