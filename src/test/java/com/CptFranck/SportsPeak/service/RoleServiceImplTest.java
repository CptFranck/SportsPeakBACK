package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.service.impl.RoleServiceImpl;
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

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createNewTestRoleList;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    RoleRepository RoleRepository;

    @InjectMocks
    RoleServiceImpl RoleServiceImpl;

    @Test
    void RoleService_Save_Success() {
        RoleEntity Role = createTestRole(null, 0);
        RoleEntity RoleSavedInRepository = createTestRole(1L, 0);
        when(RoleRepository.save(Mockito.any(RoleEntity.class))).thenReturn(RoleSavedInRepository);

        RoleEntity RoleSaved = RoleServiceImpl.save(Role);

        Assertions.assertEquals(RoleSavedInRepository, RoleSaved);
    }

    @Test
    void RoleService_FindAll_Success() {
        List<RoleEntity> RoleList = createNewTestRoleList();
        when(RoleRepository.findAll()).thenReturn(RoleList);

        List<RoleEntity> RoleFound = RoleServiceImpl.findAll();

        Assertions.assertEquals(RoleList, RoleFound);
    }

    @Test
    void RoleService_FindOne_Success() {
        RoleEntity RoleEntity = createTestRole(1L, 0);
        when(RoleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(RoleEntity));

        Optional<RoleEntity> RoleFound = RoleServiceImpl.findOne(RoleEntity.getId());

        Assertions.assertTrue(RoleFound.isPresent());
        Assertions.assertEquals(RoleEntity, RoleFound.get());
    }

    @Test
    void RoleService_FindMany_Success() {
        List<RoleEntity> RoleList = createNewTestRoleList();
        Set<Long> RoleIds = RoleList.stream().map(RoleEntity::getId).collect(Collectors.toSet());
        when(RoleRepository.findAllById(Mockito.anyIterable())).thenReturn(RoleList);

        Set<RoleEntity> RoleFound = RoleServiceImpl.findMany(RoleIds);
        Assertions.assertEquals(new HashSet<>(RoleList), RoleFound);
    }

    @Test
    void RoleService_Exists_Success() {
        RoleEntity RoleEntity = createTestRole(1L, 0);
        when(RoleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean RoleFound = RoleServiceImpl.exists(RoleEntity.getId());

        Assertions.assertTrue(RoleFound);
    }

    @Test
    void RoleService_Delete_Success() {
        RoleEntity RoleEntity = createTestRole(1L, 0);
        when(RoleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(RoleEntity));

        assertAll(() -> RoleServiceImpl.delete(RoleEntity.getId()));
    }

    @Test
    void RoleService_Delete_Unsuccessful() {
        RoleEntity RoleEntity = createTestRole(1L, 0);
        when(RoleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> RoleServiceImpl.delete(RoleEntity.getId()));
    }
}
