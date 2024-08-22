package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestRole;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_FindByName_ReturnRole() {
        RoleEntity role = createNewTestRole(0);
        RoleEntity savedRole = roleRepository.save(role);

        Optional<RoleEntity> foundRole = roleRepository.findByName(savedRole.getName());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }
}
