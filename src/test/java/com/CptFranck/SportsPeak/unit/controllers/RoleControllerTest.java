package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.RoleController;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.resolver.RoleInputResolver;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.RoleTestUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private Mapper<RoleEntity, RoleDto> roleMapper;

    @Mock
    private RoleInputResolver roleInputResolver;

    @Mock
    private RoleService roleService;

    @Mock
    private UserManager userManager;

    private RoleEntity roleEntity;
    private RoleDto roleDto;

    @BeforeEach
    void init() {
        roleEntity = createTestRole(1L, 0);
        roleDto = createTestRoleDto(1L);
    }

    @Test
    void getRoles_ValidUse_ReturnListOfRoleDto() {
        when(roleService.findAll()).thenReturn(List.of(roleEntity));
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        List<RoleDto> roleDtos = roleController.getRoles();

        Assertions.assertEquals(List.of(this.roleDto), roleDtos);
    }

    @Test
    void getRoleById_ValidUse_ReturnRoleDto() {
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        RoleDto roleDto = roleController.getRoleById(1L);

        Assertions.assertEquals(this.roleDto, roleDto);
    }

    @Test
    void addRole_ValidInput_ReturnRoleDto() {
        when(roleInputResolver.resolveInput(Mockito.any(InputNewRole.class))).thenReturn(roleEntity);
        when(userManager.saveRole(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        RoleDto roleDto = roleController.addRole(createTestInputNewRole());

        Assertions.assertEquals(this.roleDto, roleDto);
    }

    @Test
    void modifyRole_ValidInput_ReturnRoleDto() {
        when(roleInputResolver.resolveInput(Mockito.any(InputNewRole.class))).thenReturn(roleEntity);
        when(userManager.saveRole(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        RoleDto roleDto = roleController.modifyRole(createTestInputRole(1L));

        Assertions.assertEquals(this.roleDto, roleDto);
    }

    @Test
    void deleteRole_ValidInput_ReturnRoleId() {
        Long id = roleController.deleteRole(1L);

        Assertions.assertEquals(1L, id);
    }
}