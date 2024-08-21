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
    public void RoleRepository_Save_ReturnSavedRole() {
        RoleEntity role = createNewTestRole();

        RoleEntity savedRole = roleRepository.save(role);

        Assertions.assertNotNull(savedRole);
        Assertions.assertTrue(role.getId() > 0L, "Id > 0");
    }

    @Test
    public void RoleRepository_FindAll_ReturnAllRole() {
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
    public void RoleRepository_FindById_ReturnRole() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        Optional<RoleEntity> foundRole = roleRepository.findById(savedRole.getId());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }

    @Test
    public void RoleRepository_FindByName_ReturnRole() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        Optional<RoleEntity> foundRole = roleRepository.findByName(savedRole.getName());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }

    @Test
    public void RoleRepository_ExistById_ReturnTrue() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        boolean foundRole = roleRepository.existsById(savedRole.getId());

        Assertions.assertTrue(foundRole);
    }

    @Test
    public void RoleRepository_DeleteById_ReturnTrue() {
        RoleEntity role = createNewTestRole();
        RoleEntity savedRole = roleRepository.save(role);

        roleRepository.deleteById(savedRole.getId());
        boolean foundRole = roleRepository.existsById(savedRole.getId());

        Assertions.assertFalse(foundRole);
    }

}
