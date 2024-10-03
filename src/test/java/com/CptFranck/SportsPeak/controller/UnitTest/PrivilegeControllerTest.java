package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.PrivilegeController;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivilegeControllerTest {

    @InjectMocks
    private PrivilegeController privilegeController;

    @Mock
    private Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    @Mock
    private PrivilegeService privilegeService;

    @Mock
    private RoleService roleService;

    private PrivilegeEntity privilegeEntity;
    private PrivilegeDto privilegeDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        privilegeEntity = createTestPrivilege(1L);
        privilegeDto = createTestPrivilegeDto(1L);
    }

    @Test
    void PrivilegeController_GetPrivileges_Success() {
        when(privilegeService.findAll()).thenReturn(List.of(privilegeEntity));
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        List<PrivilegeDto> privilegeDtos = privilegeController.getPrivileges();

        Assertions.assertNotNull(privilegeDtos);
    }

    @Test
    void PrivilegeController_GetPrivilegeById_Unsuccessful() {
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        PrivilegeDto privilegeDto = privilegeController.getPrivilegeById(1L);

        Assertions.assertNull(privilegeDto);
    }

    @Test
    void PrivilegeController_GetPrivilegeById_Success() {
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(privilegeEntity));
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        PrivilegeDto privilegeDto = privilegeController.getPrivilegeById(1L);

        Assertions.assertNotNull(privilegeDto);
    }

    @Test
    void PrivilegeController_AddPrivilege_Success() {
        Set<RoleEntity> roles = new HashSet<>();
        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
        when(privilegeService.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        PrivilegeDto privilegeDto = privilegeController.addPrivilege(createTestInputNewPrivilege());

        Assertions.assertNotNull(privilegeDto);
    }

    @Test
    void PrivilegeController_ModifyPrivilege_UnsuccessfulDoesNotExist() {
        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(false);

        PrivilegeDto privilegeDto = privilegeController.modifyPrivilege(createTestInputPrivilege(1L));

        Assertions.assertNull(privilegeDto);
    }

    @Test
    void PrivilegeController_ModifyPrivilege_Success() {
        Set<RoleEntity> roles = new HashSet<>();
        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(privilegeEntity));
        when(privilegeService.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        PrivilegeDto privilegeDto = privilegeController.modifyPrivilege(createTestInputPrivilege(1L));

        Assertions.assertNotNull(privilegeDto);
    }

    @Test
    void PrivilegeController_DeletePrivilege_Success() {
        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(true);

        Long id = privilegeController.deletePrivilege(1L);

        Assertions.assertNotNull(id);
    }
}