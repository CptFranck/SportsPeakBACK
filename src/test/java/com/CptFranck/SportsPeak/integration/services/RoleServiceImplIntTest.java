package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class RoleServiceImplIntTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    private RoleEntity role;
    private RoleEntity roleBis;

    @BeforeEach
    public void setUp() {
        role = roleRepository.save(createTestRole(null, 0));
        roleBis = roleRepository.save(createTestRole(null, 1));
    }

    @AfterEach
    public void afterEach() {
        this.roleRepository.deleteAll();
        this.privilegeRepository.deleteAll();
    }

    @Test
    void RoleService_Save_Success() {
        RoleEntity unsavedRole = createTestRole(null, 2);

        RoleEntity roleSaved = roleServiceImpl.save(unsavedRole);

        assertEqualsRole(unsavedRole, roleSaved);
    }

    @Test
    void RoleService_Save_UpdateSuccess() {
        RoleEntity roleSaved = roleServiceImpl.save(role);

        assertEqualsRole(role, roleSaved);
    }

    @Test
    void RoleService_Save_UnSuccessful() {
        assertThrows(RoleExistsException.class, () -> roleServiceImpl.save(createTestRole(null, 0)));
    }

    @Test
    void RoleService_FindAll_Success() {
        List<RoleEntity> roleFound = roleServiceImpl.findAll();

        assertEqualsRoleList(List.of(role, roleBis), roleFound);
    }

    @Test
    void RoleService_FindOne_Success() {
        Optional<RoleEntity> roleFound = roleServiceImpl.findOne(role.getId());

        Assertions.assertTrue(roleFound.isPresent());
        assertEqualsRole(role, roleFound.get());
    }

    @Test
    void RoleService_findByName_Success() {
        Optional<RoleEntity> roleFound = roleServiceImpl.findByName(role.getName());

        Assertions.assertTrue(roleFound.isPresent());
        assertEqualsRole(role, roleFound.get());
    }

    @Test
    void RoleService_FindMany_Success() {
        Set<RoleEntity> roleFound = roleServiceImpl.findMany(Set.of(role.getId()));

        assertEqualsRoleList(List.of(role), roleFound.stream().toList());
    }

    @Test
    void exerciseService_UpdatePrivilegeRelation_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(1L, 0));
        Set<Long> oldRoleIds = Set.of(role.getId());
        Set<Long> newRoleIds = Set.of(roleBis.getId());

        roleServiceImpl.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilege);

        Optional<RoleEntity> roleOneReturn = roleRepository.findById(role.getId());
        Optional<RoleEntity> roleTwoReturn = roleRepository.findById(roleBis.getId());
        assertTrue(roleOneReturn.isPresent());
        assertTrue(roleTwoReturn.isPresent());
        assertEquals(0, roleOneReturn.get().getPrivileges().size());
        assertEquals(1, roleTwoReturn.get().getPrivileges().size());
        assertEquals(privilege.getId(), roleTwoReturn.get().getPrivileges().stream().toList().getFirst().getId());
    }

    @Test
    void RoleService_Exists_Success() {
        boolean roleFound = roleServiceImpl.exists(role.getId());

        Assertions.assertTrue(roleFound);
    }

    @Test
    void RoleService_Delete_Success() {
        assertAll(() -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void RoleService_Delete_Unsuccessful() {
        roleRepository.delete(role);

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.delete(role.getId()));
    }

    private void assertEqualsRoleList(
            List<RoleEntity> expectedRoleList,
            List<RoleEntity> roleListObtained
    ) {
        Assertions.assertEquals(expectedRoleList.size(), roleListObtained.size());
        expectedRoleList.forEach(roleFound -> assertEqualsRole(
                roleListObtained.stream().filter(
                        role -> Objects.equals(role.getId(), roleFound.getId())
                ).toList().getFirst(),
                roleFound)
        );
    }

    private void assertEqualsRole(RoleEntity expected, RoleEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }
}
