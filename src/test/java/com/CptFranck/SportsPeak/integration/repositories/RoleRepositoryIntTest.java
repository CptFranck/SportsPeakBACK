package com.CptFranck.SportsPeak.integration.repositories;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;

@DataJpaTest
public class RoleRepositoryIntTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void roleRepository_FindByName_ReturnRole() {
        RoleEntity role = createTestRole(null, 0);
        RoleEntity savedRole = roleRepository.save(role);

        Optional<RoleEntity> foundRole = roleRepository.findByName(savedRole.getName());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }
}
