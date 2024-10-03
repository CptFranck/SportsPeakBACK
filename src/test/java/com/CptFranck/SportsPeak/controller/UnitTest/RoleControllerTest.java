package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.RoleController;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private Mapper<RoleEntity, RoleDto> roleMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private PrivilegeService privilegeService;

    private RoleEntity roleEntity;
    private RoleDto roleDto;

    @BeforeEach
    void init() {
        roleEntity = createTestRole(1L, 0);
        roleDto = createTestRoleDto(1L);
    }

    @Test
    void RoleController_GetRoles_Success() {
        when(roleService.findAll()).thenReturn(List.of(roleEntity));
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        List<RoleDto> roleDtos = roleController.getRoles();

        Assertions.assertNotNull(roleDtos);
    }

    @Test
    void RoleController_GetRoleById_Unsuccessful() {
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        RoleDto roleDto = roleController.getRoleById(1L);

        Assertions.assertNull(roleDto);
    }

    @Test
    void RoleController_GetRoleById_Success() {
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(roleEntity));
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        RoleDto roleDto = roleController.getRoleById(1L);

        Assertions.assertNotNull(roleDto);
    }

    @Test
    void RoleController_AddRole_Success() {
        Set<UserEntity> users = new HashSet<>();
        Set<PrivilegeEntity> privileges = new HashSet<>();
        when(userService.findMany(Mockito.anySet())).thenReturn(users);
        when(privilegeService.findMany(Mockito.anySet())).thenReturn(privileges);
        when(roleService.save(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        RoleDto roleDto = roleController.addRole(createTestInputNewRole());

        Assertions.assertNotNull(roleDto);
    }

    @Test
    void RoleController_ModifyRole_UnsuccessfulDoesNotExist() {
        when(roleService.exists(Mockito.any(Long.class))).thenReturn(false);

        RoleDto roleDto = roleController.modifyRole(createTestInputRole(1L));

        Assertions.assertNull(roleDto);
    }

    @Test
    void RoleController_ModifyRole_Success() {
        Set<UserEntity> users = new HashSet<>();
        Set<PrivilegeEntity> privileges = new HashSet<>();
        when(roleService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(userService.findMany(Mockito.anySet())).thenReturn(users);
        when(privilegeService.findMany(Mockito.anySet())).thenReturn(privileges);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(roleEntity));
        when(roleService.save(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        RoleDto roleDto = roleController.modifyRole(createTestInputRole(1L));

        Assertions.assertNotNull(roleDto);
    }

    @Test
    void RoleController_DeleteRole_Success() {
        Long id = roleController.deleteRole(1L);

        Assertions.assertNotNull(id);
    }
}