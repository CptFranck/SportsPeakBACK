package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilegeDto;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRoleDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;

@ExtendWith(MockitoExtension.class)
public class RoleMapperImplTest {

    private final Mapper<RoleEntity, RoleDto> roleMapper;

    public RoleMapperImplTest() {
        this.roleMapper = new RoleMapperImpl(new ModelMapper());
    }

    @Test
    void roleTypeMapper_MapTo_Success() {
        RoleEntity role = createTestRole(1L, 0);
        UserEntity user = createTestUser(1L);
        PrivilegeEntity privilege = createTestPrivilege(1L, 0);
        role.getUsers().add(user);
        role.getPrivileges().add(privilege);

        RoleDto roleDto = roleMapper.mapTo(role);

        Assertions.assertEquals(role.getId(), roleDto.getId());
        Assertions.assertEquals(role.getName(), roleDto.getName());
        Assertions.assertEquals(role.getPrivileges().size(), roleDto.getPrivileges().size());
        Assertions.assertArrayEquals(
                role.getPrivileges().stream().map(PrivilegeEntity::getId).toArray(),
                roleDto.getPrivileges().stream().map(PrivilegeDto::getId).toArray()
        );
    }

    @Test
    void roleMapper_MapFrom_Success() {
        RoleDto role = createTestRoleDto(1L);
        PrivilegeDto privilege = createTestPrivilegeDto(1L);
        role.getPrivileges().add(privilege);

        RoleEntity roleEntity = roleMapper.mapFrom(role);

        Assertions.assertEquals(role.getId(), roleEntity.getId());
        Assertions.assertEquals(role.getName(), roleEntity.getName());
        Assertions.assertEquals(role.getPrivileges().size(), roleEntity.getPrivileges().size());
        Assertions.assertArrayEquals(
                role.getPrivileges().stream().map(PrivilegeDto::getId).toArray(),
                roleEntity.getPrivileges().stream().map(PrivilegeEntity::getId).toArray()
        );
    }
}
