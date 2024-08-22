package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestDataPrivilegeUtils.createNewTestPrivilege;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PrivilegeRepositoryTest {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    public void privilegeRepository_FindByName_ReturnTrue() {
        PrivilegeEntity privilege = createNewTestPrivilege();
        PrivilegeEntity savedPrivilege = privilegeRepository.save(privilege);

        Optional<PrivilegeEntity> foundPrivilege = privilegeRepository.findByName(savedPrivilege.getName());

        Assertions.assertNotNull(foundPrivilege);
        Assertions.assertTrue(foundPrivilege.isPresent());
        Assertions.assertNotNull(foundPrivilege.get());
    }
}
