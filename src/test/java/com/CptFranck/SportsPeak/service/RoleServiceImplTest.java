package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
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

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createNewTestRoleList;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleServiceImpl;

    @Test
    void RoleService_Save_Success() {
        RoleEntity role = createTestRole(null, 0);
        RoleEntity roleSavedInRepository = createTestRole(1L, 0);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(roleRepository.save(Mockito.any(RoleEntity.class))).thenReturn(roleSavedInRepository);

        RoleEntity RoleSaved = roleServiceImpl.save(role);

        Assertions.assertEquals(roleSavedInRepository, RoleSaved);
    }

    @Test
    void RoleService_Save_UpdateSuccess() {
        RoleEntity role = createTestRole(1L, 0);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(roleRepository.save(Mockito.any(RoleEntity.class))).thenReturn(role);

        RoleEntity RoleSaved = roleServiceImpl.save(role);

        Assertions.assertEquals(role, RoleSaved);
    }

    @Test
    void RoleService_Save_UnSuccessful() {
        RoleEntity role = createTestRole(1L, 0);
        RoleEntity roleAlreadyRegisteredWithSameName = createTestRole(2L, 0);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(roleAlreadyRegisteredWithSameName));

        assertThrows(RoleExistsException.class, () -> roleServiceImpl.save(role));
    }

    @Test
    void RoleService_FindAll_Success() {
        List<RoleEntity> roleList = createNewTestRoleList();
        when(roleRepository.findAll()).thenReturn(roleList);

        List<RoleEntity> roleFound = roleServiceImpl.findAll();

        Assertions.assertEquals(roleList, roleFound);
    }

    @Test
    void RoleService_FindOne_Success() {
        RoleEntity role = createTestRole(1L, 0);
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(role));

        Optional<RoleEntity> roleFound = roleServiceImpl.findOne(role.getId());

        Assertions.assertTrue(roleFound.isPresent());
        Assertions.assertEquals(role, roleFound.get());
    }

    @Test
    void RoleService_findByName_Success() {
        RoleEntity role = createTestRole(1L, 0);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));

        Optional<RoleEntity> roleFound = roleServiceImpl.findByName(role.getName());

        Assertions.assertTrue(roleFound.isPresent());
        Assertions.assertEquals(role, roleFound.get());
    }

    @Test
    void RoleService_FindMany_Success() {
        List<RoleEntity> roleList = createNewTestRoleList();
        Set<Long> roleIds = roleList.stream().map(RoleEntity::getId).collect(Collectors.toSet());
        when(roleRepository.findAllById(Mockito.anyIterable())).thenReturn(roleList);

        Set<RoleEntity> roleFound = roleServiceImpl.findMany(roleIds);
        Assertions.assertEquals(new HashSet<>(roleList), roleFound);
    }

    @Test
    void exerciseService_UpdatePrivilegeRelation_Success() {
        RoleEntity roleOne = createTestRole(1L, 1);
        RoleEntity roleTwo = createTestRole(2L, 2);
        Set<Long> oldRoleIds = new HashSet<>();
        Set<Long> newRoleIds = new HashSet<>();
        oldRoleIds.add(roleOne.getId());
        newRoleIds.add(roleTwo.getId());

        PrivilegeEntity privilege = createTestPrivilege(1L);

        when(roleRepository.findAllById(oldRoleIds)).thenReturn(List.of(roleOne));
        when(roleRepository.findAllById(newRoleIds)).thenReturn(List.of(roleTwo));

        assertAll(() -> roleServiceImpl.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilege));
    }

    @Test
    void RoleService_Exists_Success() {
        RoleEntity roleEntity = createTestRole(1L, 0);
        when(roleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean roleFound = roleServiceImpl.exists(roleEntity.getId());

        Assertions.assertTrue(roleFound);
    }

    @Test
    void RoleService_Delete_Success() {
        RoleEntity role = createTestRole(1L, 0);
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(role));

        assertAll(() -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void RoleService_Delete_Unsuccessful() {
        RoleEntity role = createTestRole(1L, 0);
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.delete(role.getId()));
    }
}
