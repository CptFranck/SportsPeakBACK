package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.mapper.impl.PrivilegeMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestPrivilegeDto;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRoleDto;

public class PrivilegeMapperImplIT {

    private final Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    public PrivilegeMapperImplIT() {
        this.privilegeMapper = new PrivilegeMapperImpl(new ModelMapper());
    }

    @Test
    void privilegeTypeMapper_MapTo_Success() {
        PrivilegeEntity privilege = createTestPrivilege(1L, 0);
        RoleEntity role = createTestRole(1L, 0);
        privilege.getRoles().add(role);

        PrivilegeDto privilegeDto = privilegeMapper.mapTo(privilege);

        Assertions.assertEquals(privilege.getId(), privilegeDto.getId());
        Assertions.assertEquals(privilege.getName(), privilegeDto.getName());
        Assertions.assertEquals(privilege.getRoles().size(), privilegeDto.getRoles().size());
        Assertions.assertArrayEquals(
                privilege.getRoles().stream().map(RoleEntity::getId).toArray(),
                privilegeDto.getRoles().stream().map(RoleDto::getId).toArray()
        );
    }

    @Test
    void privilegeTypeMapper_MapFrom_Success() {
        PrivilegeDto privilege = createTestPrivilegeDto(1L);
        RoleDto role = createTestRoleDto(1L);
        privilege.getRoles().add(role);

        PrivilegeEntity privilegeEntity = privilegeMapper.mapFrom(privilege);

        Assertions.assertEquals(privilege.getId(), privilegeEntity.getId());
        Assertions.assertEquals(privilege.getName(), privilegeEntity.getName());
        Assertions.assertEquals(privilege.getRoles().size(), privilegeEntity.getRoles().size());
        Assertions.assertArrayEquals(
                privilege.getRoles().stream().map(RoleDto::getId).toArray(),
                privilegeEntity.getRoles().stream().map(RoleEntity::getId).toArray()
        );
    }
}
