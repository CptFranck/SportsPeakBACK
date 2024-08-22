package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

import java.util.HashSet;

public class TestDataRoleUtils {

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

    public static RoleDto createTestRoleDto() {
        return new RoleDto(
                9L,
                "Role name",
                new HashSet<UserDto>(),
                new HashSet<PrivilegeDto>()
        );
    }
}
