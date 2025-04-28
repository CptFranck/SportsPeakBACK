package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestPrivilegeUtils {

    public static PrivilegeEntity createTestPrivilege(Long id, int option) {
        String privilegeName = "Privilege name";
        if (option == 1) {
            privilegeName = "Privilege One";
        } else if (option == 2) {
            privilegeName = "Privilege Two";
        }
        return new PrivilegeEntity(
                id,
                privilegeName,
                new HashSet<RoleEntity>()
        );
    }


    public static List<PrivilegeEntity> createNewTestPrivilegeList(boolean nullIds) {
        if (nullIds)
            return List.of(createTestPrivilege(null, 1), createTestPrivilege(null, 2));
        else
            return List.of(createTestPrivilege(1L, 1), createTestPrivilege(2L, 2));
    }

    public static PrivilegeDto createTestPrivilegeDto(Long id) {
        return new PrivilegeDto(
                id,
                "Privilege name",
                new HashSet<RoleDto>()
        );
    }

    public static InputNewPrivilege createTestInputNewPrivilege() {
        return new InputNewPrivilege(
                "name",
                new ArrayList<Long>()
        );
    }

    public static InputPrivilege createTestInputPrivilege(Long id) {
        return new InputPrivilege(
                id,
                "name",
                new ArrayList<Long>()
        );
    }
}
