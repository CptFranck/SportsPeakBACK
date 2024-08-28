package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

import java.util.HashSet;
import java.util.List;

public class TestRoleUtils {

    public static RoleEntity createTestRole(Long id, int option) {
        String roleName = "Role name";
        if (option == 1) {
            roleName = "Role One";
        } else if (option == 2) {
            roleName = "Role Two";
        }
        return new RoleEntity(
                id,
                roleName,
                new HashSet<UserEntity>(),
                new HashSet<PrivilegeEntity>()
        );
    }

    public static List<RoleEntity> createNewTestRoleList() {
        return List.of(
                createTestRole(1L, 1),
                createTestRole(2L, 2)
        );
    }

    public static RoleDto createTestRoleDto() {
        return new RoleDto(
                9L,
                "Role name",
                new HashSet<UserDto>(),
                new HashSet<PrivilegeDto>()
        );
    }
}