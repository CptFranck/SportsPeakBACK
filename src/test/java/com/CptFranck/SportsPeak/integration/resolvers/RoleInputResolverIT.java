package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.resolver.RoleInputResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestInputNewRole;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestInputRole;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class RoleInputResolverIT {

    @Autowired
    private RoleInputResolver roleInputResolver;

    @Test
    void resolveInput_ValidInputNewPrivilege_ReturnPrivilegeEntity() {
        InputNewRole inputNewRole = createTestInputNewRole();

        RoleEntity roleResolved = roleInputResolver.resolveInput(inputNewRole);

        assertRoleInputAndEntity(inputNewRole, roleResolved);
    }

    @Test
    void resolveInput_ValidInputPrivilege_ReturnPrivilegeEntity() {
        InputRole inputRole = createTestInputRole(1L);

        RoleEntity roleResolved = roleInputResolver.resolveInput(inputRole);

        Assertions.assertEquals(inputRole.getId(), roleResolved.getId());
        assertRoleInputAndEntity(inputRole, roleResolved);
    }

    private void assertRoleInputAndEntity(InputNewRole expectedRole, RoleEntity actualRole) {
        Assertions.assertEquals(expectedRole.getName(), actualRole.getName());
        Assertions.assertEquals(expectedRole.getPrivilegeIds().size(), actualRole.getPrivileges().size());
    }
}
