package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.PrivilegeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestPrivilege;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PrivilegeServiceImplIT {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegeServiceImpl privilegeServiceImpl;

    private PrivilegeEntity privilege;

    @BeforeEach
    public void setUp() {
        privilege = privilegeRepository.save(createTestPrivilege(null, 0));
    }

    @AfterEach
    public void afterEach() {
        this.privilegeRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfPrivilegeEntity() {
        List<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findAll();

        assertEqualPrivilegeList(List.of(privilege), privilegeFound);
    }

    @Test
    void findOne_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        privilegeRepository.delete(privilege);

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.findOne(privilege.getId()));
    }

    @Test
    void findOne_ValidPrivilegeId_ReturnPrivilegeEntity() {
        PrivilegeEntity privilegeFound = privilegeServiceImpl.findOne(privilege.getId());

        assertEqualPrivilege(privilege, privilegeFound);
    }

    @Test
    void findMany_ValidPrivilegeIds_ReturnSetOfPrivilegeEntity() {
        Set<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findMany(Set.of(privilege.getId()));

        assertEqualPrivilegeList(List.of(privilege), privilegeFound.stream().toList());
    }

    @Test
    void save_AddNewPrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null, 0);

        assertThrows(PrivilegeExistsException.class, () -> privilegeServiceImpl.save(unsavedPrivilege));
    }

    @Test
    void save_AddNewPrivilege_ReturnPrivilegeEntity() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null, 1);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(unsavedPrivilege);

        assertEqualPrivilege(unsavedPrivilege, privilegeSaved);
    }

    @Test
    void save_UpdatePrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        privilege.setId(privilege.getId() + 1);

        assertThrows(PrivilegeExistsException.class, () -> privilegeServiceImpl.save(privilege));
    }

    @Test
    void save_UpdatePrivilegeWithInvalidId_ThrowPrivilegeNotFoundException() {
        privilegeRepository.delete(privilege);

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.save(privilege));
    }

    @Test
    void save_UpdatePrivilege_ReturnPrivilegeEntity() {
        privilege.setName("other privilege name");
        privilegeRepository.save(privilege);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(privilege);

        assertEqualPrivilege(privilege, privilegeSaved);
    }

    @Test
    void delete_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        privilegeRepository.delete(privilege);

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean privilegeFound = privilegeServiceImpl.exists(privilege.getId());

        Assertions.assertTrue(privilegeFound);
    }

    private void assertEqualPrivilegeList(
            List<PrivilegeEntity> expectedPrivilegeList,
            List<PrivilegeEntity> privilegeListObtained
    ) {
        Assertions.assertEquals(expectedPrivilegeList.size(), privilegeListObtained.size());
        expectedPrivilegeList.forEach(privilegeFound -> assertEqualPrivilege(
                privilegeListObtained.stream().filter(
                        privilege -> Objects.equals(privilege.getId(), privilegeFound.getId())
                ).toList().getFirst(),
                privilegeFound)
        );
    }

    private void assertEqualPrivilege(PrivilegeEntity expected, PrivilegeEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }
}
