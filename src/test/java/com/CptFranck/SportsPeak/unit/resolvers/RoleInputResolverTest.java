package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.resolver.RoleInputResolver;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestInputNewRole;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestInputRole;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleInputResolverTest {

    @InjectMocks
    private RoleInputResolver roleInputResolver;

    @Mock
    private PrivilegeService privilegeService;

    @Mock
    private UserService userService;

    @Test
    void resolveInput_ValidInputNewPrivilege_ReturnPrivilegeEntity() {
        InputNewRole inputNewRole = createTestInputNewRole();
        when(userService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(privilegeService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        RoleEntity roleResolved = roleInputResolver.resolveInput(inputNewRole);

        assertRoleInputAndEntity(inputNewRole, roleResolved);
    }

    @Test
    void resolveInput_ValidInputPrivilege_ReturnPrivilegeEntity() {
        InputRole inputRole = createTestInputRole(1L);
        when(userService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(privilegeService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());

        RoleEntity roleResolved = roleInputResolver.resolveInput(inputRole);

        Assertions.assertEquals(inputRole.getId(), roleResolved.getId());
        assertRoleInputAndEntity(inputRole, roleResolved);
    }

    private void assertRoleInputAndEntity(InputNewRole expectedRole, RoleEntity actualRole) {
        Assertions.assertEquals(expectedRole.getName(), actualRole.getName());
        Assertions.assertEquals(expectedRole.getPrivilegeIds().size(), actualRole.getPrivileges().size());
    }
}
