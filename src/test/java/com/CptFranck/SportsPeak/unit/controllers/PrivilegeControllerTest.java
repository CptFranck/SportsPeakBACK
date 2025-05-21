package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.PrivilegeController;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.resolver.PrivilegeInputResolver;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivilegeControllerTest {

    @InjectMocks
    private PrivilegeController privilegeController;

    @Mock
    private Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    @Mock
    private PrivilegeInputResolver privilegeInputResolver;

    @Mock
    private PrivilegeService privilegeService;

    @Mock
    private RoleManager roleManager;


    private PrivilegeEntity privilegeEntity;
    private PrivilegeDto privilegeDto;

    @BeforeEach
    void init() {
        privilegeEntity = createTestPrivilege(1L, 0);
        privilegeDto = createTestPrivilegeDto(1L);
    }

    @Test
    void getPrivileges_ValidUse_ReturnListOfPrivilegeDto() {
        when(privilegeService.findAll()).thenReturn(List.of(privilegeEntity));
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        List<PrivilegeDto> privilegeDtos = privilegeController.getPrivileges();

        Assertions.assertEquals(List.of(this.privilegeDto), privilegeDtos);
    }

    @Test
    void getPrivilegeById_ValidUse_ReturnPrivilegeDto() {
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        PrivilegeDto privilegeDto = privilegeController.getPrivilegeById(1L);

        Assertions.assertEquals(this.privilegeDto, privilegeDto);
    }

    @Test
    void addPrivilege_ValidInput_ReturnPrivilegeDto() {
        when(privilegeInputResolver.resolveInput(Mockito.any(InputNewPrivilege.class))).thenReturn(privilegeEntity);
        when(roleManager.savePrivilege(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        PrivilegeDto privilegeDto = privilegeController.addPrivilege(createTestInputNewPrivilege());

        Assertions.assertEquals(this.privilegeDto, privilegeDto);
    }

    @Test
    void modifyPrivilege_ValidInput_ReturnPrivilegeDto() {
        when(privilegeInputResolver.resolveInput(Mockito.any(InputPrivilege.class))).thenReturn(privilegeEntity);
        when(roleManager.savePrivilege(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        PrivilegeDto privilegeDto = privilegeController.modifyPrivilege(createTestInputPrivilege(1L));

        Assertions.assertEquals(this.privilegeDto, privilegeDto);
    }

    @Test
    void deletePrivilege_ValidInput_ReturnPrivilegeId() {
        Long id = privilegeController.deletePrivilege(1L);

        Assertions.assertEquals(1L, id);
    }
}