package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestRole;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_Save_ReturnSavedMuscle() {
        RoleEntity role = createNewTestRole();

        RoleEntity savedRole = roleRepository.save(role);

        Assertions.assertNotNull(savedRole);
        Assertions.assertTrue(role.getId() > 0L, "Id > 0");
    }

    @Test
    public void RoleRepository_findAll_ReturnAllMuscle() {
        RoleEntity roleOne = createNewTestRole();
        RoleEntity roleTwo = createNewTestRole();
        roleOne.setName("roleOne");
        roleTwo.setName("roleTwo");
        roleRepository.save(roleOne);
        roleRepository.save(roleTwo);

        List<RoleEntity> roleEntities = StreamSupport.stream(
                        roleRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(roleEntities);
        Assertions.assertEquals(roleEntities.size(), 2);
    }

    @Test
    public void RoleRepository_findById_ReturnMuscle() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        Optional<RoleEntity> foundMuscle = roleRepository.findById(savedRole.getId());

        Assertions.assertNotNull(foundMuscle);
        Assertions.assertTrue(foundMuscle.isPresent());
        Assertions.assertNotNull(foundMuscle.get());
    }

    @Test
    public void RoleRepository_existById_ReturnTrue() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        boolean foundMuscle = roleRepository.existsById(savedRole.getId());

        Assertions.assertTrue(foundMuscle);
    }

    @Test
    public void RoleRepository_deleteById_ReturnTrue() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        roleRepository.deleteById(savedRole.getId());
        boolean foundMuscle = roleRepository.existsById(savedRole.getId());

        Assertions.assertFalse(foundMuscle);
    }
}
