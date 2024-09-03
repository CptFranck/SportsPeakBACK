package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestUserUtils {

    public static UserEntity createTestUser(Long id) {
        return new UserEntity(
                id,
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

    public static List<UserEntity> createTestUserList() {
        return List.of(
                createTestUser(1L),
                createTestUser(2L));
    }

    public static UserDto createTestUserDto(Long id) {
        return new UserDto(
                id,
                "test@test.test",
                "John",
                "Doe",
                "John_Doe",
                new HashSet<RoleDto>(),
                new HashSet<ProgExerciseDto>(),
                new HashSet<ProgExerciseDto>()
        );
    }

    public static InputUserIdentity createTestInputUserIdentity(Long id) {
        return new InputUserIdentity(
                id,
                "FirstName",
                "LastName"
        );
    }

    public static InputUserRoles createTestInputUserRoles(Long id) {
        return new InputUserRoles(
                id,
                new ArrayList<Long>()

        );
    }

    public static InputUserEmail createTestInputUserEmail(Long id) {
        return new InputUserEmail(
                id,
                "Password",
                "newemail@test.com"
        );
    }

    public static InputUserUsername createTestInputUserUsername(Long id) {
        return new InputUserUsername(
                id,
                "newUsername"
        );
    }

    public static InputUserPassword createTestInputUserPassword(Long id) {
        return new InputUserPassword(
                id,
                "OldPassword",
                "NewPassword"
        );
    }
}
