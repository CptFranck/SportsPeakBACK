package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;

import java.util.HashSet;

public class TestDataPrivilegeUtils {

    public static PrivilegeEntity createTestPrivilege() {
        return new PrivilegeEntity(
                8L,
                "Privilege name",
                new HashSet<RoleEntity>()
        );
    }

    public static PrivilegeEntity createNewTestPrivilege() {
        return new PrivilegeEntity(
                null,
                "Privilege name",
                new HashSet<RoleEntity>()
        );
    }

    public static PrivilegeDto createTestPrivilegeDto() {
        return new PrivilegeDto(
                8L,
                "Privilege name",
                new HashSet<RoleDto>()
        );
    }
}
