package com.CptFranck.SportsPeak.integration.repositories;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.repository.PrivilegeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.createTestPrivilege;

@DataJpaTest
public class PrivilegeRepositoryIntTest {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    public void privilegeRepository_FindByName_ReturnTrue() {
        PrivilegeEntity savedPrivilege = privilegeRepository.save(createTestPrivilege(null, 0));

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findByName(savedPrivilege.getName());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }
}
