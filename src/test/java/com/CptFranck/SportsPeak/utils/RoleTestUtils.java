package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RoleTestUtils {

    public static RoleEntity createTestRole(Long id, int option) {
        String roleName = "USER";
        if (option == 1) {
            roleName = "Role One";
        } else if (option == 2) {
            roleName = "Role Two";
        }
        return new RoleEntity(
                id,
                roleName,
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static List<RoleEntity> createNewTestRoleList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestRole(null, 1), createTestRole(null, 2));
        else
            return List.of(createTestRole(1L, 1), createTestRole(2L, 2));
    }

    public static RoleDto createTestRoleDto(Long id) {
        return new RoleDto(
                id,
                "Role name",
                new HashSet<>()
        );
    }

    public static InputNewRole createTestInputNewRole() {
        return new InputNewRole(
                "name",
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static InputRole createTestInputRole(Long id) {
        return new InputRole(
                id,
                "name",
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static void assertEqualsRole(RoleEntity expected, RoleEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    public static void assertRoleDtoAndEntity(RoleEntity roleEntity, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(roleEntity.getId(), roleDto.getId());
        Assertions.assertEquals(roleEntity.getName(), roleDto.getName());
        Assertions.assertEquals(roleEntity.getPrivileges().size(), roleDto.getPrivileges().size());
    }

    public static void assertRoleDtoAndInput(InputNewRole inputNewRole, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(inputNewRole.getName(), roleDto.getName());
        Assertions.assertEquals(inputNewRole.getPrivilegeIds().size(), roleDto.getPrivileges().size());
    }
}
