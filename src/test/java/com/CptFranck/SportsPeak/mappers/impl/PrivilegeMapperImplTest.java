package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestDataPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.domain.utils.TestDataPrivilegeUtils.createTestPrivilegeDto;
import static com.CptFranck.SportsPeak.domain.utils.TestDataRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestDataRoleUtils.createTestRoleDto;

@ExtendWith(MockitoExtension.class)
public class PrivilegeMapperImplTest {

    private final Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    public PrivilegeMapperImplTest() {
        this.privilegeMapper = new PrivilegeMapperImpl(new ModelMapper());
    }

    @Test
    void exerciseTypeMapper_MapTo_Success() {
        PrivilegeEntity privilege = createTestPrivilege();
        RoleEntity role = createTestRole();
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
    void exerciseTypeMapper_MapFrom_Success() {
        PrivilegeDto privilege = createTestPrivilegeDto();
        RoleDto role = createTestRoleDto();
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
