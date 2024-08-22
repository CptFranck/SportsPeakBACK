package com.CptFranck.SportsPeak.domain;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

import java.util.HashSet;

public class TestDataUserUtils {

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

    public static UserEntity createNewTestUser() {
        return new UserEntity(
                null,
                "test@test.test",
                "firstName",
                "lastName",
                "userName",
                "password",
                new HashSet<RoleEntity>(),
                new HashSet<ProgExerciseEntity>(),
                new HashSet<ProgExerciseEntity>()
        );
    }

    public static UserDto createTestUserDto() {
        return new UserDto(
                4L,
                "test@test.test",
                "John",
                "Doe",
                "John_Doe",
                new HashSet<RoleDto>(),
                new HashSet<ProgExerciseDto>(),
                new HashSet<ProgExerciseDto>()
        );
    }
}
