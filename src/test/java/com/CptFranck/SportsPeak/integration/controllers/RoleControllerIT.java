package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.RoleController;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.utils.RoleTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class RoleControllerIT {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleController roleController;

    private RoleEntity role;

    @BeforeEach
    void setUp() {
        role = roleRepository.save(createTestRole(null, 0));
    }

    @AfterEach
    public void afterEach() {
        this.roleRepository.deleteAll();
    }

    @Test
    void getRoles_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.getRoles());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getRoles_ValidUse_ReturnListOfRoleDto() {
        List<RoleDto> roleDtos = roleController.getRoles();

        assertEqualRoleList(List.of(role), roleDtos);
    }

    @Test
    void getRoleById_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.getRoleById(role.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getRoleById_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);

        Assertions.assertThrows(RoleNotFoundException.class, () -> roleController.getRoleById(role.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getRoleById_ValidUse_ReturnRoleDto() {
        RoleDto roleDto = roleController.getRoleById(role.getId());

        assertRoleDtoAndEntity(role, roleDto);
    }

    @Test
    void addRole_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputNewRole inputNewRole = createTestInputNewRole();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.addRole(inputNewRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addRole_AddNewRoleWithNameAlreadyTaken_ThrowRoleExistsException() {
        InputNewRole inputNewRole = createTestInputNewRole();
        inputNewRole.setName(role.getName());

        assertThrows(RoleExistsException.class, () -> roleController.addRole(inputNewRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addRole_ValidInput_ReturnRoleDto() {
        InputNewRole inputNewRole = createTestInputNewRole();

        RoleDto roleDto = roleController.addRole(inputNewRole);

        assertRoleDtoAndInput(inputNewRole, roleDto);
    }

    @Test
    void modifyRole_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputRole inputRole = createTestInputRole(role.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.modifyRole(inputRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyRole_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);
        InputRole inputRole = createTestInputRole(role.getId());

        assertThrows(RoleNotFoundException.class, () -> roleController.modifyRole(inputRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyRole_ValidInput_ReturnRoleDto() {
        InputRole inputRole = createTestInputRole(role.getId());
        inputRole.setName(role.getName());

        RoleDto roleDto = roleController.modifyRole(inputRole);

        assertRoleDtoAndInput(inputRole, roleDto);
    }

    @Test
    void deleteRole_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.deleteRole(role.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteRole_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);

        Assertions.assertThrows(RoleNotFoundException.class, () -> roleController.deleteRole(role.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteRole_ValidInput_ReturnRoleId() {
        Long id = roleController.deleteRole(role.getId());

        Assertions.assertEquals(role.getId(), id);
    }

    private void assertEqualRoleList(
            List<RoleEntity> roleEntities,
            List<RoleDto> roleDtos
    ) {
        Assertions.assertEquals(roleEntities.size(), roleDtos.size());
        roleEntities.forEach(roleEntity -> assertRoleDtoAndEntity(
                roleEntity,
                roleDtos.stream().filter(
                        roleDto -> Objects.equals(roleDto.getId(), roleEntity.getId())
                ).toList().getFirst())
        );
    }
}