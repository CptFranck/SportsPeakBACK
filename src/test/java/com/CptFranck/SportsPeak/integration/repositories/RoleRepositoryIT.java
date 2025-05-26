package com.CptFranck.SportsPeak.integration.repositories;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;

@DataJpaTest
public class RoleRepositoryIT {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void roleRepository_FindByName_ReturnRole() {
        RoleEntity savedRole = roleRepository.save(createTestRole(null, 0));

        Optional<RoleEntity> foundRole = roleRepository.findByName(savedRole.getName());

        Assertions.assertNotNull(foundRole);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertNotNull(foundRole.get());
    }
}
