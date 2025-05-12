package com.CptFranck.SportsPeak.unit.services;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.PrivilegeServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createNewTestPrivilegeList;
import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.createTestPrivilege;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrivilegeServiceImplTest {

    @InjectMocks
    private PrivilegeServiceImpl privilegeServiceImpl;

    @Mock
    private PrivilegeRepository privilegeRepository;

    private PrivilegeEntity privilege;

    @BeforeEach
    void setUp() {
        privilege = createTestPrivilege(1L, 0);
    }

    @Test
    void findAll_ValidUse_ReturnListOfPrivilegeEntity() {
        List<PrivilegeEntity> privilegeList = createNewTestPrivilegeList(false);
        when(privilegeRepository.findAll()).thenReturn(privilegeList);

        List<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findAll();

        Assertions.assertEquals(privilegeList, privilegeFound);
    }

    @Test
    void findOne_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.findOne(privilege.getId()));
    }

    @Test
    void findOne_ValidPrivilegeId_ReturnPrivilegeEntity() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(privilege));

        PrivilegeEntity privilegeFound = privilegeServiceImpl.findOne(privilege.getId());

        Assertions.assertEquals(privilege, privilegeFound);
    }

    @Test
    void findMany_ValidPrivilegeIds_ReturnSetOfPrivilegeEntity() {
        List<PrivilegeEntity> privilegeList = createNewTestPrivilegeList(false);
        Set<Long> privilegeIds = privilegeList.stream().map(PrivilegeEntity::getId).collect(Collectors.toSet());
        when(privilegeRepository.findAllById(Mockito.anyIterable())).thenReturn(privilegeList);

        Set<PrivilegeEntity> privilegeFound = privilegeServiceImpl.findMany(privilegeIds);
        Assertions.assertEquals(new HashSet<>(privilegeList), privilegeFound);
    }

    @Test
    void save_AddNewPrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null, 0);
        when(privilegeRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(privilege));

        assertThrows(PrivilegeExistsException.class, () -> privilegeServiceImpl.save(unsavedPrivilege));
    }

    @Test
    void save_AddNewPrivilege_ReturnPrivilegeEntity() {
        PrivilegeEntity unsavedPrivilege = createTestPrivilege(null, 0);
        when(privilegeRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(privilegeRepository.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilege);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(unsavedPrivilege);

        Assertions.assertEquals(privilege, privilegeSaved);
    }

    @Test
    void save_UpdatePrivilegeWithInvalidId_ThrowPrivilegeNotFoundException() {
        when(privilegeRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(privilege));
        when(privilegeRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.save(privilege));
    }

    @Test
    void save_UpdatePrivilege_ReturnPrivilegeEntity() {
        when(privilegeRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(privilege));
        when(privilegeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(privilegeRepository.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilege);

        PrivilegeEntity privilegeSaved = privilegeServiceImpl.save(privilege);

        Assertions.assertEquals(privilege, privilegeSaved);
    }

    @Test
    void delete_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(privilege));

        assertAll(() -> privilegeServiceImpl.delete(privilege.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(privilegeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean privilegeFound = privilegeServiceImpl.exists(privilege.getId());

        Assertions.assertTrue(privilegeFound);
    }
}
