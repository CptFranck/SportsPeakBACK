package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void setUp() {
        privilegeRepository.deleteAll();
    }

    @Test
    public void MuscleRepository_Save_ReturnSavedMuscle() {
        PrivilegeEntity privilege = createNewTestPrivilege();

        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Assertions.assertNotNull(savedPrivilege);
        Assertions.assertTrue(privilege.getId() > 0L, "Id > 0");
    }

    @Test
    public void MuscleRepository_findAll_ReturnAllMuscle() {
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
    public void MuscleRepository_findById_ReturnMuscle() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Optional<PrivilegeEntity> foundMuscle = privilegeRepository.findById(savedPrivilege.getId());

        Assertions.assertNotNull(foundMuscle);
        Assertions.assertTrue(foundMuscle.isPresent());
        Assertions.assertNotNull(foundMuscle.get());
    }

    @Test
    public void MuscleRepository_existById_ReturnTrue() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        boolean foundMuscle = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertTrue(foundMuscle);
    }

    @Test
    public void MuscleRepository_deleteById_ReturnTrue() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        privilegeRepository.deleteById(savedPrivilege.getId());
        boolean foundMuscle = privilegeRepository.existsById(savedPrivilege.getId());

        Assertions.assertFalse(foundMuscle);
    }
}
