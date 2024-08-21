package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestPrivilege;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PrivilegeRepositoryTest {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    public void PrivilegeRepository_Save_ReturnSavedPrivilege() {
        PrivilegeEntity privilege = createNewTestPrivilege();

        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Assertions.assertNotNull(savedPrivilege);
        Assertions.assertTrue(privilege.getId() > 0L, "Id > 0");
    }

    @Test
    public void PrivilegeRepository_FindAll_ReturnAllPrivilege() {
        PrivilegeEntity privilegeOne = createNewTestPrivilege();
        PrivilegeEntity privilegeTwo = createNewTestPrivilege();
        privilegeOne.setName("privilegeOne");
        privilegeTwo.setName("privilegeTwo");
        privilegeRepository.save(privilegeOne);
        privilegeRepository.save(privilegeTwo);

        List<PrivilegeEntity> privilegeEntities = StreamSupport.stream(
                        privilegeRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(privilegeEntities);
        Assertions.assertEquals(privilegeEntities.size(), 2);
    }

    @Test
    public void PrivilegeRepository_FindById_ReturnPrivilege() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findById(savedPrivilege.getId());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }

    @Test
    public void PrivilegeRepository_ExistByName_ReturnTrue() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findByName(savedPrivilege.getName());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }


    @Test
    public void PrivilegeRepository_ExistById_ReturnTrue() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        boolean foundPrivilege = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertTrue(foundPrivilege);
    }

    @Test
    public void PrivilegeRepository_DeleteById_ReturnTrue() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        privilegeRepository.deleteById(savedPrivilege.getId());
        boolean foundPrivilege = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertFalse(foundPrivilege);
    }
}
