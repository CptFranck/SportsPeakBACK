package com.CptFranck.SportsPeak.unit.services.managers;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.managerImpl.RoleManagerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.createTestPrivilege;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleManagerImplTest {

    @InjectMocks
    private RoleManagerImpl roleManager;

    @Mock
    private PrivilegeService privilegeService;

    @Mock
    private RoleService roleService;

    @Test
    void savePrivilege_AddNewPrivilege_ReturnPrivilegeEntity() {
        PrivilegeEntity privilege = createTestPrivilege(null, 0);
        when(privilegeService.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilege);

        PrivilegeEntity privilegeSaved = roleManager.savePrivilege(privilege);

        Assertions.assertEquals(privilege, privilegeSaved);
    }

    @Test
    void savePrivilege_UpdatePrivilege_ReturnPrivilegeEntity() {
        PrivilegeEntity privilege = createTestPrivilege(1L, 0);
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(privilege);
        when(privilegeService.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilege);

        PrivilegeEntity privilegeSaved = roleManager.savePrivilege(privilege);

        Assertions.assertEquals(privilege, privilegeSaved);
    }
}
