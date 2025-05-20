package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestUserUtils {

    public static void assertEqualsUser(UserEntity userToGet, UserEntity obtainedUser, boolean justBeenCreated) {
        if (!justBeenCreated)
            Assertions.assertEquals(userToGet.getId(), obtainedUser.getId());
        Assertions.assertEquals(userToGet.getEmail(), obtainedUser.getEmail());
        Assertions.assertEquals(userToGet.getFirstName(), obtainedUser.getFirstName());
        Assertions.assertEquals(userToGet.getLastName(), obtainedUser.getLastName());
        Assertions.assertEquals(userToGet.getUsername(), obtainedUser.getUsername());
        Assertions.assertEquals(userToGet.getPassword(), obtainedUser.getPassword());
        Assertions.assertEquals(userToGet.getRoles().size(), obtainedUser.getRoles().size());
        Assertions.assertEquals(userToGet.getSubscribedProgExercises().size(), obtainedUser.getSubscribedProgExercises().size());
        Assertions.assertEquals(userToGet.getProgExercisesCreated().size(), obtainedUser.getProgExercisesCreated().size());
    }

    public static UserEntity createTestUser(Long id) {
        return new UserEntity(
                id,
                "john.doe@email.test",
                "John",
                "Doe",
                "John_Doe",
                "password",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static UserEntity createTestUserBis(Long id) {
        return new UserEntity(
                id,
                "jane.doe@email.test",
                "Jane",
                "Doe",
                "Jane_Doe",
                "password",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static List<UserEntity> createTestUserList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestUser(null), createTestUserBis(null));
        else
            return List.of(createTestUser(1L), createTestUserBis(2L));
    }

    public static UserDto createTestUserDto(Long id) {
        return new UserDto(
                id,
                "test@test.test",
                "John",
                "Doe",
                "John_Doe",
                new HashSet<>()
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
                new ArrayList<>()

        );
    }

    public static InputUserEmail createTestInputUserEmail(Long id, String rawPassword) {
        return new InputUserEmail(
                id,
                rawPassword,
                "newemail@test.com"
        );
    }

    public static InputUserUsername createTestInputUserUsername(Long id) {
        return new InputUserUsername(
                id,
                "newUsername"
        );
    }

    public static InputUserPassword createTestInputUserPassword(Long id, String olRawPassword) {
        return new InputUserPassword(
                id,
                olRawPassword,
                "NewPassword"
        );
    }
}
