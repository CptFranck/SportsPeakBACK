package com.CptFranck.SportsPeak.service.IntegrationTest;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.service.impl.RoleServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createNewTestRoleList;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class RoleServiceImplIntTest {


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @AfterEach
    public void afterEach() {
        this.privilegeRepository.deleteAll();
        this.roleRepository.deleteAll();
    }

    @Test
    void RoleService_Save_Success() {
        RoleEntity unsavedRole = createTestRole(null, 0);

        RoleEntity roleSaved = roleServiceImpl.save(unsavedRole);

        assertEqualsRole(unsavedRole, roleSaved);
    }

    @Test
    void RoleService_Save_UpdateSuccess() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        RoleEntity roleSaved = roleServiceImpl.save(role);

        assertEqualsRole(role, roleSaved);
    }

    @Test
    void RoleService_Save_UnSuccessful() {
        roleRepository.save(createTestRole(null, 0));

        assertThrows(RoleExistsException.class, () -> roleServiceImpl.save(createTestRole(null, 0)));
    }

    @Test
    void RoleService_FindAll_Success() {
        List<RoleEntity> roleList = StreamSupport.stream(
                roleRepository.saveAll(createNewTestRoleList(true)).spliterator(),
                false).toList();

        List<RoleEntity> roleFound = roleServiceImpl.findAll();

        assertEqualsRoleList(roleList, roleFound);
    }

    @Test
    void RoleService_FindOne_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        Optional<RoleEntity> roleFound = roleServiceImpl.findOne(role.getId());

        Assertions.assertTrue(roleFound.isPresent());
        assertEqualsRole(role, roleFound.get());
    }

    @Test
    void RoleService_findByName_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        Optional<RoleEntity> roleFound = roleServiceImpl.findByName(role.getName());

        Assertions.assertTrue(roleFound.isPresent());
        assertEqualsRole(role, roleFound.get());
    }

    @Test
    void RoleService_FindMany_Success() {
        List<RoleEntity> roleList = StreamSupport.stream(
                roleRepository.saveAll(createNewTestRoleList(true)).spliterator(),
                false).toList();
        Set<Long> roleIds = roleList.stream().map(RoleEntity::getId).collect(Collectors.toSet());

        Set<RoleEntity> roleFound = roleServiceImpl.findMany(roleIds);

        assertEqualsRoleList(roleList, roleFound.stream().toList());
    }

    @Test
    @Transactional
    void exerciseService_UpdatePrivilegeRelation_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(1L, 0));
        RoleEntity roleOne = roleRepository.save(createTestRole(1L, 1));
        RoleEntity roleTwo = roleRepository.save(createTestRole(2L, 2));
        Set<Long> oldRoleIds = new HashSet<>();
        Set<Long> newRoleIds = new HashSet<>();
        oldRoleIds.add(roleOne.getId());
        newRoleIds.add(roleTwo.getId());
        roleOne.getPrivileges().add(privilege);
        roleRepository.save(roleOne);

        roleServiceImpl.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilege);

        assertEquals(0, roleOne.getPrivileges().size());
        assertEquals(1, roleTwo.getPrivileges().size());
        assertEquals(privilege.getId(), roleTwo.getPrivileges().stream().toList().getFirst().getId());
    }

    @Test
    void RoleService_Exists_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        boolean roleFound = roleServiceImpl.exists(role.getId());

        Assertions.assertTrue(roleFound);
    }

    @Test
    void RoleService_Delete_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        assertAll(() -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void RoleService_Delete_Unsuccessful() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        roleRepository.delete(role);

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.delete(role.getId()));
    }

    private void assertEqualsRoleList(
            List<RoleEntity> expectedRoleList,
            List<RoleEntity> roleListObtained
    ) {
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
