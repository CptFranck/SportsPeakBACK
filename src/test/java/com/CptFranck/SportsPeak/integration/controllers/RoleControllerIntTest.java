package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.RoleController;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
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

import static com.CptFranck.SportsPeak.utils.TestRoleUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class RoleControllerIntTest {

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
    void RoleController_GetRoles_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.getRoles());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_GetRoles_Success() {
        List<RoleDto> roleDtos = roleController.getRoles();

        assertEqualRoleList(List.of(role), roleDtos);
    }

    @Test
    void RoleController_GetRoleById_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.getRoleById(role.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_GetRoleById_UnsuccessfulRoleNotFound() {
        Assertions.assertThrows(RoleNotFoundException.class, () -> roleController.getRoleById(role.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_GetRoleById_Success() {
        RoleDto roleDto = roleController.getRoleById(role.getId());

        assertRoleDtoAndEntity(role, roleDto);
    }

    @Test
    void RoleController_AddRole_UnsuccessfulNotAuthenticated() {
        InputNewRole inputNewRole = createTestInputNewRole();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.addRole(inputNewRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_AddRole_Success() {
        InputNewRole inputNewRole = createTestInputNewRole();

        RoleDto roleDto = roleController.addRole(inputNewRole);

        assertRoleDtoAndInput(inputNewRole, roleDto);
    }

    @Test
    void RoleController_ModifyRole_UnsuccessfulNotAuthenticated() {
        InputRole inputRole = createTestInputRole(role.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.modifyRole(inputRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_ModifyRole_UnsuccessfulRoleNotFound() {
        InputRole inputRole = createTestInputRole(role.getId() + 1);

        Assertions.assertThrows(RoleNotFoundException.class, () -> roleController.modifyRole(inputRole));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_ModifyRole_Success() {
        InputRole inputRole = createTestInputRole(role.getId());

        RoleDto roleDto = roleController.modifyRole(inputRole);

        assertRoleDtoAndInput(inputRole, roleDto);
    }

    @Test
    void RoleController_DeleteExercise_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> roleController.deleteRole(role.getId()));
    }
//    @Test
//    @WithMockUser(username = "user", roles = "ADMIN")
//    void RoleController_DeleteExercise_UnsuccessfulRoleNotFound() {
//        Assertions.assertThrows(RoleNotFoundException.class, () -> roleController.deleteRole(role.getId() + 1));
//    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_DeleteRole_Success() {
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

    private void assertRoleDtoAndEntity(RoleEntity roleEntity, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(roleEntity.getName(), roleDto.getName());
        Assertions.assertEquals(roleEntity.getPrivileges().size(), roleDto.getPrivileges().size());
    }

    private void assertRoleDtoAndInput(InputNewRole inputNewRole, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(inputNewRole.getName(), roleDto.getName());
        Assertions.assertEquals(inputNewRole.getPrivilegeIds().size(), roleDto.getPrivileges().size());
    }
}