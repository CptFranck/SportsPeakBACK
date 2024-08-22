package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
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
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createTestUserDto;

@ExtendWith(MockitoExtension.class)
public class RoleMapperImplTest {

    private final Mapper<RoleEntity, RoleDto> roleMapper;

    public RoleMapperImplTest() {
        this.roleMapper = new RoleMapperImpl(new ModelMapper());
    }

    @Test
    void roleTypeMapper_MapTo_Success() {
        RoleEntity role = createTestRole();
        UserEntity user = createTestUser();
        PrivilegeEntity privilege = createTestPrivilege();
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
        Assertions.assertEquals(role.getUsers().size(), roleDto.getUsers().size());
        Assertions.assertArrayEquals(
                role.getUsers().stream().map(UserEntity::getId).toArray(),
                roleDto.getUsers().stream().map(UserDto::getId).toArray()
        );
    }

    @Test
    void roleMapper_MapFrom_Success() {
        RoleDto role = createTestRoleDto();
        UserDto user = createTestUserDto();
        PrivilegeDto privilege = createTestPrivilegeDto();
        role.getUsers().add(user);
        role.getPrivileges().add(privilege);

        RoleEntity roleEntity = roleMapper.mapFrom(role);

        Assertions.assertEquals(role.getId(), roleEntity.getId());
        Assertions.assertEquals(role.getName(), roleEntity.getName());
        Assertions.assertEquals(role.getPrivileges().size(), roleEntity.getPrivileges().size());
        Assertions.assertArrayEquals(
                role.getPrivileges().stream().map(PrivilegeDto::getId).toArray(),
                roleEntity.getPrivileges().stream().map(PrivilegeEntity::getId).toArray()
        );
        Assertions.assertEquals(role.getUsers().size(), roleEntity.getUsers().size());
        Assertions.assertArrayEquals(
                role.getUsers().stream().map(UserDto::getId).toArray(),
                roleEntity.getUsers().stream().map(UserEntity::getId).toArray()
        );
    }
}
