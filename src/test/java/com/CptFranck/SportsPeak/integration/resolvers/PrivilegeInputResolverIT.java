package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.resolver.PrivilegeInputResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.createTestInputNewPrivilege;
import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.createTestInputPrivilege;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PrivilegeInputResolverIT {

    @Autowired
    private PrivilegeInputResolver privilegeInputResolver;

    @Test
    void resolveInput_ValidInputNewPrivilege_ReturnPrivilegeEntity() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();

        PrivilegeEntity privilegeSaved = privilegeInputResolver.resolveInput(inputNewPrivilege);

        assertPrivilegeInputAndEntity(inputNewPrivilege, privilegeSaved);
    }

    @Test
    void resolveInput_ValidInputPrivilege_ReturnPrivilegeEntity() {
        InputPrivilege inputPrivilege = createTestInputPrivilege(1L);

        PrivilegeEntity muscleSaved = privilegeInputResolver.resolveInput(inputPrivilege);

        Assertions.assertEquals(inputPrivilege.getId(), muscleSaved.getId());
        assertPrivilegeInputAndEntity(inputPrivilege, muscleSaved);
    }

    private void assertPrivilegeInputAndEntity(InputNewPrivilege expectedPrivilege, PrivilegeEntity actualPrivilege) {
        Assertions.assertEquals(expectedPrivilege.getName(), actualPrivilege.getName());
        Assertions.assertEquals(expectedPrivilege.getRoleIds().size(), actualPrivilege.getRoles().size());
    }
}
