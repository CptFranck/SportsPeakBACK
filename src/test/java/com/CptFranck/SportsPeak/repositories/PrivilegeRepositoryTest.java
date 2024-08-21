package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.TestDataUtil;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
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

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestPrivilege;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestPrivileges;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PrivilegeRepositoryTest {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    private PrivilegeEntity saveOnePrivilegeInRepository() {
        PrivilegeEntity user = createNewTestPrivilege(0);
        return privilegeRepository.save(user);
    }

    private List<PrivilegeEntity> saveAllPrivilegesInRepository(List<PrivilegeEntity> users) {
        List<PrivilegeEntity> localUsers = Objects.requireNonNullElseGet(users, TestDataUtil::createNewTestPrivileges);
        privilegeRepository.saveAll(localUsers);
        return localUsers;
    }

    @Test
    public void PrivilegeRepository_Save_ReturnSavedPrivilege() {
        PrivilegeEntity privilege = createNewTestPrivilege(0);

        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Assertions.assertNotNull(savedPrivilege);
        Assertions.assertTrue(privilege.getId() > 0L, "Id > 0");
    }

    @Test
    public void PrivilegeRepository_SaveAll_ReturnSavedPrivileges() {
        List<PrivilegeEntity> privileges = createNewTestPrivileges();

        List<PrivilegeEntity> savedPrivileges = saveAllPrivilegesInRepository(privileges);

        Assertions.assertNotNull(savedPrivileges);
        Assertions.assertEquals(privileges.size(), savedPrivileges.size());
        Assertions.assertTrue(privileges.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(privileges.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void PrivilegeRepository_FindById_ReturnPrivilege() {
        PrivilegeEntity savedPrivilege = saveOnePrivilegeInRepository();

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findById(savedPrivilege.getId());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }

    @Test
    public void PrivilegeRepository_FindAll_ReturnAllPrivilege() {
        saveAllPrivilegesInRepository(null);

        List<PrivilegeEntity> privilegeEntities = StreamSupport.stream(
                        privilegeRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(privilegeEntities);
        Assertions.assertEquals(privilegeEntities.size(), 2);
    }

    @Test
    public void PrivilegeRepository_FindAllById_ReturnAllUsers() {
        List<PrivilegeEntity> users = createNewTestPrivileges();
        privilegeRepository.saveAll(users);

        List<PrivilegeEntity> userEntities = StreamSupport.stream(
                        privilegeRepository.findAllById(users.stream()
                                        .map(PrivilegeEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(2, userEntities.size());
    }

    @Test
    public void PrivilegeRepository_FindByName_ReturnTrue() {
        PrivilegeEntity savedPrivilege = saveOnePrivilegeInRepository();

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findByName(savedPrivilege.getName());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }


    @Test
    public void PrivilegeRepository_ExistById_ReturnTrue() {
        PrivilegeEntity savedPrivilege = saveOnePrivilegeInRepository();

        boolean foundPrivilege = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertTrue(foundPrivilege);
    }

    @Test
    public void UserRepository_Delete_ReturnFalse() {
        PrivilegeEntity savedPrivilege = saveOnePrivilegeInRepository();

        privilegeRepository.delete(savedPrivilege);
        boolean foundUser = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertFalse(foundUser);
    }

    @Test
    public void PrivilegeRepository_DeleteById_ReturnFalse() {
        PrivilegeEntity savedPrivilege = saveOnePrivilegeInRepository();

        privilegeRepository.deleteById(savedPrivilege.getId());
        boolean foundPrivilege = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertFalse(foundPrivilege);
    }

    @Test
    public void PrivilegeRepository_DeleteAllById_ReturnAllFalse() {
        List<PrivilegeEntity> privilegeEntities = saveAllPrivilegesInRepository(null);

        privilegeRepository.deleteAllById(privilegeEntities.stream().map(PrivilegeEntity::getId).toList());

        privilegeEntities.forEach(privilege -> {
            boolean foundPrivilege = privilegeRepository.existsById(privilege.getId());
            Assertions.assertFalse(foundPrivilege);
        });
    }

    @Test
    public void PrivilegeRepository_DeleteAll_ReturnAllFalse() {
        List<PrivilegeEntity> privilegeEntities = saveAllPrivilegesInRepository(null);

        privilegeRepository.deleteAll();

        privilegeEntities.forEach(privilege -> {
            boolean foundPrivilege = privilegeRepository.existsById(privilege.getId());
            Assertions.assertFalse(foundPrivilege);
        });
    }
}
