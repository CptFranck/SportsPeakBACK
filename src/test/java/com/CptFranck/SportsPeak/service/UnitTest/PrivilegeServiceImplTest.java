package com.CptFranck.SportsPeak.service.UnitTest;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.impl.PrivilegeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createNewTestPrivilegeList;
import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrivilegeServiceImplTest {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private PrivilegeServiceImpl privilegeServiceImpl;

    private PrivilegeEntity privilege;

    @BeforeEach
    void setUp() {
        privilege = createTestPrivilege(1L);
    }

    @Test
    void PrivilegeService_Save_Success() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null);
        when(privilegeRepository.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilege);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(unsavedPrivilege);

        Assertions.assertEquals(privilege, privilegeSaved);
    }

    @Test
    void PrivilegeService_Save_UpdateSuccess() {
        when(privilegeRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(privilege));
        when(privilegeRepository.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilege);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(privilege);

        Assertions.assertEquals(privilege, privilegeSaved);
    }

    @Test
    void PrivilegeService_Save_UnSuccessful() {
        PrivilegeEntity privilegeAlreadyRegisteredWithSameName = createTestPrivilege(2L);
        when(privilegeRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(privilegeAlreadyRegisteredWithSameName));

        assertThrows(PrivilegeExistsException.class, () -> privilegeServiceImpl.save(privilege));
    }

    @Test
    void PrivilegeService_FindAll_Success() {
        List<PrivilegeEntity> privilegeList = createNewTestPrivilegeList();
        when(privilegeRepository.findAll()).thenReturn(privilegeList);

        List<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findAll();

        Assertions.assertEquals(privilegeList, privilegeFound);
    }

    @Test
    void PrivilegeService_FindOne_Success() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(privilege));

        Optional<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findOne(privilege.getId());

        Assertions.assertTrue(privilegeFound.isPresent());
        Assertions.assertEquals(privilege, privilegeFound.get());
    }

    @Test
    void PrivilegeService_FindMany_Success() {
        List<PrivilegeEntity> privilegeList = createNewTestPrivilegeList();
        Set<Long> privilegeIds = privilegeList.stream().map(PrivilegeEntity::getId).collect(Collectors.toSet());
        when(privilegeRepository.findAllById(Mockito.anyIterable())).thenReturn(privilegeList);

        Set<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findMany(privilegeIds);
        Assertions.assertEquals(new HashSet<>(privilegeList), privilegeFound);
    }

    @Test
    void PrivilegeService_Exists_Success() {
        when(privilegeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean privilegeFound = privilegeServiceImpl.exists(privilege.getId());

        Assertions.assertTrue(privilegeFound);
    }

    @Test
    void PrivilegeService_Delete_Success() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(privilege));

        assertAll(() -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void PrivilegeService_Delete_Unsuccessful() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.delete(privilege.getId()));
    }
}
