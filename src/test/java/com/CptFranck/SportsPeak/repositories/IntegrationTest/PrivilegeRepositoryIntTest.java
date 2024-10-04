package com.CptFranck.SportsPeak.repositories.IntegrationTest;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;

@DataJpaTest
public class PrivilegeRepositoryIntTest {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    public void privilegeRepository_FindByName_ReturnTrue() {
        PrivilegeEntity privilege = createTestPrivilege(null);
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findByName(savedPrivilege.getName());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }
}
