package com.CptFranck.SportsPeak.integration.service;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.impl.PrivilegeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createNewTestPrivilegeList;
import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PrivilegeServiceImplIntTest {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegeServiceImpl privilegeServiceImpl;

    @AfterEach
    public void afterEach() {
        this.privilegeRepository.deleteAll();
    }

    @Test
    void PrivilegeService_Save_Success() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null, 0);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(unsavedPrivilege);

        assertEqualPrivilege(unsavedPrivilege, privilegeSaved);
    }

    @Test
    void PrivilegeService_Save_UpdateSuccess() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));
        PrivilegeEntity privilegeBis = createTestPrivilege(null, 0);
        privilegeBis.setName("other privilege name");
        privilegeRepository.save(privilegeBis);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(privilege);

        assertEqualPrivilege(privilege, privilegeSaved);
    }

    @Test
    void PrivilegeService_Save_UnSuccessful() {
        privilegeServiceImpl.save(createTestPrivilege(null, 0));

        assertThrows(PrivilegeExistsException.class, () -> privilegeServiceImpl.save(createTestPrivilege(null, 0)));
    }

    @Test
    void PrivilegeService_FindAll_Success() {
        List<PrivilegeEntity> privilegeList = StreamSupport.stream(
                privilegeRepository.saveAll(createNewTestPrivilegeList(true)).spliterator(),
                false).toList();

        List<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findAll();

        assertEqualPrivilegeList(privilegeList, privilegeFound);
    }

    @Test
    void PrivilegeService_FindOne_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));

        Optional<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findOne(privilege.getId());

        Assertions.assertTrue(privilegeFound.isPresent());
        assertEqualPrivilege(privilege, privilegeFound.get());
    }

    @Test
    void PrivilegeService_FindMany_Success() {
        List<PrivilegeEntity> privilegeList = StreamSupport.stream(
                privilegeRepository.saveAll(createNewTestPrivilegeList(true)).spliterator(),
                false).toList();
        Set<Long> privilegeIds = privilegeList.stream().map(PrivilegeEntity::getId).collect(Collectors.toSet());

        Set<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findMany(privilegeIds);

        assertEqualPrivilegeList(privilegeList, privilegeFound.stream().toList());
    }

    @Test
    void PrivilegeService_Exists_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));

        boolean privilegeFound = privilegeServiceImpl.exists(privilege.getId());

        Assertions.assertTrue(privilegeFound);
    }

    @Test
    void PrivilegeService_Delete_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));

        assertAll(() -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void PrivilegeService_Delete_Unsuccessful() {
        PrivilegeEntity privilege = privilegeServiceImpl.save(createTestPrivilege(null, 0));
        privilegeRepository.delete(privilege);

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.delete(privilege.getId()));
    }

    private void assertEqualPrivilegeList(
            List<PrivilegeEntity> expectedPrivilegeList,
            List<PrivilegeEntity> privilegeListObtained
    ) {
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
