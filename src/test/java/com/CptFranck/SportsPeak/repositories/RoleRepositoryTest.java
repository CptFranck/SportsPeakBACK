package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.TestDataUtil;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestRole;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestRoles;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity saveOneRoleInRepository() {
        RoleEntity role = createNewTestRole(0);
        return roleRepository.save(role);
    }

    private List<RoleEntity> saveAllRolesInRepository(List<RoleEntity> users) {
        List<RoleEntity> localRoles = Objects.requireNonNullElseGet(users, TestDataUtil::createNewTestRoles);
        roleRepository.saveAll(localRoles);
        return localRoles;
    }

    @Test
    public void RoleRepository_Save_ReturnSavedRole() {
        RoleEntity role = createNewTestRole(0);

        RoleEntity savedRole = roleRepository.save(role);

        Assertions.assertNotNull(savedRole);
        Assertions.assertTrue(role.getId() > 0L, "Id > 0");
    }

    @Test
    public void RoleRepository_SaveAll_ReturnSavedRoles() {
        List<RoleEntity> roles = createNewTestRoles();

        List<RoleEntity> savedRole = saveAllRolesInRepository(roles);

        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(roles.size(), savedRole.size());
        Assertions.assertTrue(roles.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(roles.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void RoleRepository_FindById_ReturnRole() {
        RoleEntity savedRole = saveOneRoleInRepository();

        Optional<RoleEntity> foundRole = roleRepository.findById(savedRole.getId());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }

    @Test
    public void RoleRepository_FindAll_ReturnAllRole() {
        List<RoleEntity> roles = createNewTestRoles();
        roleRepository.saveAll(roles);

        List<RoleEntity> roleEntities = StreamSupport.stream(
                        roleRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(roleEntities);
        Assertions.assertEquals(roleEntities.size(), 2);
    }

    @Test
    public void RoleRepository_FindAllById_ReturnAllRoles() {
        List<RoleEntity> roles = createNewTestRoles();
        roleRepository.saveAll(roles);

        List<RoleEntity> userEntities = StreamSupport.stream(
                        roleRepository.findAllById(roles.stream()
                                        .map(RoleEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(2, userEntities.size());
    }

    @Test
    public void RoleRepository_FindByName_ReturnRole() {
        RoleEntity role = createNewTestRole(0);
        RoleEntity savedRole = roleRepository.save(role);

        Optional<RoleEntity> foundRole = roleRepository.findByName(savedRole.getName());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }

    @Test
    public void RoleRepository_ExistById_ReturnTrue() {
        RoleEntity role = createNewTestRole(0);
        RoleEntity savedRole = roleRepository.save(role);

        boolean foundRole = roleRepository.existsById(savedRole.getId());

        Assertions.assertTrue(foundRole);
    }

    @Test
    public void RoleRepositoryCount_ReturnUsersListSize() {
        List<RoleEntity> savedUsers = saveAllRolesInRepository(null);

        Long userCount = roleRepository.count();

        Assertions.assertEquals(userCount, savedUsers.size());
    }

    @Test
    public void RoleRepositoryDelete_ReturnFalse() {
        RoleEntity savedUser = saveOneRoleInRepository();

        roleRepository.delete(savedUser);
        boolean foundUser = roleRepository.existsById(savedUser.getId());

        Assertions.assertFalse(foundUser);
    }
    
    @Test
    public void RoleRepository_DeleteById_ReturnTrue() {
        RoleEntity role = createNewTestRole(0);
        RoleEntity savedRole = roleRepository.save(role);

        roleRepository.deleteById(savedRole.getId());
        boolean foundRole = roleRepository.existsById(savedRole.getId());

        Assertions.assertFalse(foundRole);
    }

    @Test
    public void RoleRepositoryDeleteAllById_ReturnAllFalse() {
        List<RoleEntity> userEntities = saveAllRolesInRepository(null);

        roleRepository.deleteAllById(userEntities.stream().map(RoleEntity::getId).toList());

        userEntities.forEach(user -> {
            boolean foundUser = roleRepository.existsById(user.getId());
            Assertions.assertFalse(foundUser);
        });
    }

    @Test
    public void RoleRepositoryDeleteAll_ReturnAllFalse() {
        List<RoleEntity> userEntities = saveAllRolesInRepository(null);

        roleRepository.deleteAll();

        userEntities.forEach(user -> {
            boolean foundUser = roleRepository.existsById(user.getId());
            Assertions.assertFalse(foundUser);
        });
    }
}
