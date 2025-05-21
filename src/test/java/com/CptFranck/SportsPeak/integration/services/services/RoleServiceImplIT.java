package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.repository.PrivilegeRepository;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.RoleServiceImpl;
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
public class RoleServiceImplIT {

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
    void findAll_ValidUse_ReturnListOfRoleEntity() {
        List<RoleEntity> roleFound = roleServiceImpl.findAll();

        assertEqualsRoleList(List.of(role, roleBis), roleFound);
    }

    @Test
    void findOne_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.findOne(role.getId()));
    }

    @Test
    void findOne_ValidRoleId_ReturnRoleEntity() {
        RoleEntity roleFound = roleServiceImpl.findOne(role.getId());

        assertEqualsRole(role, roleFound);
    }

    @Test
    void findByName_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.findByName(role.getName()));
    }

    @Test
    void findByName_ValidRoleId_ReturnOptionalOfRoleEntity() {
        RoleEntity roleFound = roleServiceImpl.findByName(role.getName());

        assertEqualsRole(role, roleFound);
    }

    @Test
    void findMany_ValidRoleIds_ReturnSetOfRoleEntity() {
        Set<RoleEntity> roleFound = roleServiceImpl.findMany(Set.of(role.getId()));

        assertEqualsRoleList(List.of(role), roleFound.stream().toList());
    }

    @Test
    void save_AddNewRoleWithNameAlreadyTaken_ThrowRoleExistsException() {
        RoleEntity unsavedRole = createTestRole(null, 0);

        assertThrows(RoleExistsException.class, () -> roleServiceImpl.save(unsavedRole));
    }

    @Test
    void save_AddNewRole_ReturnRoleEntity() {
        RoleEntity unsavedRole = createTestRole(null, 2);

        RoleEntity roleSaved = roleServiceImpl.save(unsavedRole);

        assertEqualsRole(unsavedRole, roleSaved);
    }

    @Test
    void save_UpdateRoleWithInvalidId_ThrowRoleNotFoundException() {
        role.setId(role.getId() + roleBis.getId() + 1);
        role.setName("test");

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.save(role));
    }

    @Test
    void save_UpdateRole_ReturnRoleEntity() {
        RoleEntity roleSaved = roleServiceImpl.save(role);

        assertEqualsRole(role, roleSaved);
    }

    @Test
    void delete_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void updateRoleRelation_ValidUse_Void() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));
        Set<Long> oldRoleIds = Set.of(role.getId());
        Set<Long> newRoleIds = Set.of(roleBis.getId());
        role.getPrivileges().add(privilege);
        roleRepository.save(role);

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
    void exists_ValidInput_ReturnTrue() {
        boolean roleFound = roleServiceImpl.exists(role.getId());

        Assertions.assertTrue(roleFound);
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
