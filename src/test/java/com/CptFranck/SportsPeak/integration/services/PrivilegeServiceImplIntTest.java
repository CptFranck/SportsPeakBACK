package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.impl.PrivilegeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestPrivilege;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PrivilegeServiceImplIntTest {

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
    void PrivilegeService_Save_Success() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null, 1);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(unsavedPrivilege);

        assertEqualPrivilege(unsavedPrivilege, privilegeSaved);
    }

    @Test
    void PrivilegeService_Save_UpdateSuccess() {
        privilege.setName("other privilege name");
        privilegeRepository.save(privilege);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(privilege);

        assertEqualPrivilege(privilege, privilegeSaved);
    }

    @Test
    void PrivilegeService_Save_UnSuccessful() {
        assertThrows(PrivilegeExistsException.class, () -> privilegeServiceImpl.save(createTestPrivilege(null, 0)));
    }

    @Test
    void PrivilegeService_FindAll_Success() {
        List<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findAll();

        assertEqualPrivilegeList(List.of(privilege), privilegeFound);
    }

    @Test
    void PrivilegeService_FindOne_Success() {
        Optional<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findOne(privilege.getId());

        Assertions.assertTrue(privilegeFound.isPresent());
        assertEqualPrivilege(privilege, privilegeFound.get());
    }

    @Test
    void PrivilegeService_FindMany_Success() {
        Set<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findMany(Set.of(privilege.getId()));

        assertEqualPrivilegeList(List.of(privilege), privilegeFound.stream().toList());
    }

    @Test
    void PrivilegeService_Exists_Success() {
        boolean privilegeFound = privilegeServiceImpl.exists(privilege.getId());

        Assertions.assertTrue(privilegeFound);
    }

    @Test
    void PrivilegeService_Delete_Success() {
        assertAll(() -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void PrivilegeService_Delete_Unsuccessful() {
        privilegeRepository.delete(privilege);

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.delete(privilege.getId()));
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
