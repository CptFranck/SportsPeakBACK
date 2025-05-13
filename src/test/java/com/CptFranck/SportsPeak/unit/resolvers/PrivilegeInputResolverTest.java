package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.resolvers.PrivilegeInputResolver;
import com.CptFranck.SportsPeak.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestInputNewPrivilege;
import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestInputPrivilege;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrivilegeInputResolverTest {

    @InjectMocks
    private PrivilegeInputResolver privilegeInputResolver;

    @Mock
    private RoleService roleService;

    @Test
    void resolveInput_ValidInputNewPrivilege_ReturnPrivilegeEntity() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();
        when(roleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        PrivilegeEntity privilegeSaved = privilegeInputResolver.resolveInput(inputNewPrivilege);

        assertPrivilegeInputAndEntity(inputNewPrivilege, privilegeSaved);
    }

    @Test
    void resolveInput_ValidInputPrivilege_ReturnPrivilegeEntity() {
        InputPrivilege inputPrivilege = createTestInputPrivilege(1L);
        when(roleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        PrivilegeEntity privilegeSaved = privilegeInputResolver.resolveInput(inputPrivilege);

        Assertions.assertEquals(inputPrivilege.getId(), privilegeSaved.getId());
        assertPrivilegeInputAndEntity(inputPrivilege, privilegeSaved);
    }

    private void assertPrivilegeInputAndEntity(InputNewPrivilege expectedPrivilege, PrivilegeEntity actualPrivilege) {
        Assertions.assertEquals(expectedPrivilege.getName(), actualPrivilege.getName());
        Assertions.assertEquals(expectedPrivilege.getRoleIds().size(), actualPrivilege.getRoles().size());
    }
}
