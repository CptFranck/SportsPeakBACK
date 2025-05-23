package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.RoleServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createNewTestRoleList;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    private RoleEntity role;

    @BeforeEach
    void setUp() {
        role = createTestRole(1L, 0);
    }

    @Test
    void findAll_ValidUse_ReturnListOfRoleEntity() {
        List<RoleEntity> roleList = createNewTestRoleList(false);
        when(roleRepository.findAll()).thenReturn(roleList);

        List<RoleEntity> roleFound = roleServiceImpl.findAll();

        Assertions.assertEquals(roleList, roleFound);
    }

    @Test
    void findOne_InvalidRoleId_ThrowRoleNotFoundException() {
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.findOne(role.getId()));
    }

    @Test
    void findOne_ValidRoleId_ReturnRoleEntity() {
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(role));

        RoleEntity roleFound = roleServiceImpl.findOne(role.getId());

        Assertions.assertEquals(role, roleFound);
    }

    @Test
    void findByName_InvalidRoleName_ThrowRoleNotFoundException() {
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.findByName(role.getName()));
    }

    @Test
    void findByName_ValidRoleName_ReturnOptionalOfRoleEntity() {
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));

        RoleEntity roleFound = roleServiceImpl.findByName(role.getName());

        Assertions.assertEquals(role, roleFound);
    }

    @Test
    void findMany_ValidRoleIds_ReturnSetOfRoleEntity() {
        List<RoleEntity> roleList = createNewTestRoleList(false);
        Set<Long> roleIds = roleList.stream().map(RoleEntity::getId).collect(Collectors.toSet());
        when(roleRepository.findAllById(Mockito.anyIterable())).thenReturn(roleList);

        Set<RoleEntity> roleFound = roleServiceImpl.findMany(roleIds);
        Assertions.assertEquals(new HashSet<>(roleList), roleFound);
    }


    @Test
    void save_AddNewRoleWithNameAlreadyTaken_ThrowRoleExistsException() {
        RoleEntity unsavedRole = createTestRole(null, 0);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));

        assertThrows(RoleExistsException.class, () -> roleServiceImpl.save(unsavedRole));
    }

    @Test
    void save_AddNewRole_ReturnRoleEntity() {
        RoleEntity unsavedRole = createTestRole(null, 0);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(roleRepository.save(Mockito.any(RoleEntity.class))).thenReturn(role);

        RoleEntity roleSaved = roleServiceImpl.save(unsavedRole);

        Assertions.assertEquals(role, roleSaved);
    }

    @Test
    void save_UpdateRoleWithInvalidId_ThrowRoleNotFoundException() {
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(roleRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.save(role));
    }

    @Test
    void save_UpdateRole_ReturnRoleEntity() {
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(roleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(roleRepository.save(Mockito.any(RoleEntity.class))).thenReturn(role);

        RoleEntity roleSaved = roleServiceImpl.save(role);

        Assertions.assertEquals(role, roleSaved);
    }

    @Test
    void delete_InvalidRoleId_ThrowRoleNotFoundException() {
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(roleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(role));

        assertAll(() -> roleServiceImpl.delete(role.getId()));
    }

    @Test
    void updateRoleRelation_ValidUse_Void() {
        RoleEntity roleOne = createTestRole(1L, 1);
        RoleEntity roleTwo = createTestRole(2L, 2);
        Set<Long> oldRoleIds = Set.of(roleOne.getId());
        Set<Long> newRoleIds = Set.of(roleTwo.getId());
        PrivilegeEntity privilege = createTestPrivilege(1L, 0);
        roleOne.getPrivileges().add(privilege);
        when(roleRepository.findAllById(oldRoleIds)).thenReturn(List.of(roleOne));
        when(roleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(roleRepository.findAllById(newRoleIds)).thenReturn(List.of(roleTwo));

        assertAll(() -> roleServiceImpl.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilege));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(roleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean roleFound = roleServiceImpl.exists(role.getId());

        Assertions.assertTrue(roleFound);
    }
}
