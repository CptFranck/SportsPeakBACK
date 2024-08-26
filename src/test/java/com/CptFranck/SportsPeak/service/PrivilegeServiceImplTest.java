package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.impl.PrivilegeServiceImpl;
import org.junit.jupiter.api.Assertions;
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
    PrivilegeRepository privilegeRepository;

    @InjectMocks
    PrivilegeServiceImpl privilegeServiceImpl;

    @Test
    void PrivilegeService_Save_Success() {
        PrivilegeEntity Privilege = createTestPrivilege(null);
        PrivilegeEntity PrivilegeSavedInRepository = createTestPrivilege(1L);
        when(privilegeRepository.save(Mockito.any(PrivilegeEntity.class))).thenReturn(PrivilegeSavedInRepository);

        PrivilegeEntity PrivilegeSaved = privilegeServiceImpl.save(Privilege);

        Assertions.assertEquals(PrivilegeSavedInRepository, PrivilegeSaved);
    }

    @Test
    void PrivilegeService_FindAll_Success() {
        List<PrivilegeEntity> PrivilegeList = createNewTestPrivilegeList();
        when(privilegeRepository.findAll()).thenReturn(PrivilegeList);

        List<PrivilegeEntity> PrivilegeFound = privilegeServiceImpl.findAll();

        Assertions.assertEquals(PrivilegeList, PrivilegeFound);
    }

    @Test
    void PrivilegeService_FindOne_Success() {
        PrivilegeEntity PrivilegeEntity = createTestPrivilege(1L);
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(PrivilegeEntity));

        Optional<PrivilegeEntity> PrivilegeFound = privilegeServiceImpl.findOne(PrivilegeEntity.getId());

        Assertions.assertTrue(PrivilegeFound.isPresent());
        Assertions.assertEquals(PrivilegeEntity, PrivilegeFound.get());
    }

    @Test
    void PrivilegeService_FindMany_Success() {
        List<PrivilegeEntity> PrivilegeList = createNewTestPrivilegeList();
        Set<Long> PrivilegeIds = PrivilegeList.stream().map(PrivilegeEntity::getId).collect(Collectors.toSet());
        when(privilegeRepository.findAllById(Mockito.anyIterable())).thenReturn(PrivilegeList);

        Set<PrivilegeEntity> PrivilegeFound = privilegeServiceImpl.findMany(PrivilegeIds);
        Assertions.assertEquals(new HashSet<>(PrivilegeList), PrivilegeFound);
    }

    @Test
    void PrivilegeService_Exists_Success() {
        PrivilegeEntity PrivilegeEntity = createTestPrivilege(1L);
        when(privilegeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean PrivilegeFound = privilegeServiceImpl.exists(PrivilegeEntity.getId());

        Assertions.assertTrue(PrivilegeFound);
    }

    @Test
    void PrivilegeService_Delete_Success() {
        PrivilegeEntity PrivilegeEntity = createTestPrivilege(1L);
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(PrivilegeEntity));

        assertAll(() -> privilegeServiceImpl.delete(PrivilegeEntity.getId()));
    }

    @Test
    void PrivilegeService_Delete_Unsuccessful() {
        PrivilegeEntity PrivilegeEntity = createTestPrivilege(1L);
        when(privilegeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PrivilegeNotFoundException.class, () -> privilegeServiceImpl.delete(PrivilegeEntity.getId()));
    }
}
