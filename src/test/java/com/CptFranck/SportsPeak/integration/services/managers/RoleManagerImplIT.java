package com.CptFranck.SportsPeak.integration.services.managers;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.repository.PrivilegeRepository;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.service.managerImpl.RoleManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.Objects;

import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.assertEqualPrivilege;
import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class RoleManagerImplIT {

    @Autowired
    private RoleManagerImpl roleManager;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    public void AfterEach() {
        roleRepository.findAll().forEach(role -> {
            role.setPrivileges(Collections.emptySet());
            roleRepository.save(role);
        });
        privilegeRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void savePrivilege_AddNewPrivilege_ReturnPrivilegeEntity() {
        PrivilegeEntity privilege = createTestPrivilege(null, 0);

        PrivilegeEntity privilegeResolved = roleManager.savePrivilege(privilege);

        assertEqualPrivilege(privilege, privilegeResolved);
    }

    @Test
    void savePrivilege_UpdatePrivilege_ReturnPrivilegeEntity() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        RoleEntity roleBis = roleRepository.save(createTestRole(null, 1));
        role.getPrivileges().add(privilege);
        roleRepository.save(role);
        privilege.getRoles().add(roleBis);

        PrivilegeEntity privilegeResolved = roleManager.savePrivilege(privilege);

        privilege = privilegeRepository.findById(privilege.getId()).orElseThrow();
        assertEqualPrivilege(privilege, privilegeResolved);
        Assertions.assertTrue(privilegeResolved.getRoles().stream().noneMatch(r -> Objects.equals(role.getId(), r.getId())));
        Assertions.assertTrue(privilegeResolved.getRoles().stream().anyMatch(r -> Objects.equals(roleBis.getId(), r.getId())));
    }
}
