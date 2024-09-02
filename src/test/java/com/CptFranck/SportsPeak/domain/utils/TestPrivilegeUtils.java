package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;

import java.util.HashSet;
import java.util.List;

public class TestPrivilegeUtils {

    public static PrivilegeEntity createTestPrivilege(Long id) {
        return new PrivilegeEntity(
                id,
                "Privilege name",
                new HashSet<RoleEntity>()
        );
    }


    public static List<PrivilegeEntity> createNewTestPrivilegeList() {
        return List.of(
                createTestPrivilege(1L),
                createTestPrivilege(2L)
        );
    }

    public static PrivilegeDto createTestPrivilegeDto(Long id) {
        return new PrivilegeDto(
                id,
                "Privilege name",
                new HashSet<RoleDto>()
        );
    }
}
