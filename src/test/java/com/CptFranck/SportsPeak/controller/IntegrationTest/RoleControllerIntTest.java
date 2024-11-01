package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.controller.RoleController;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class RoleControllerIntTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleController roleController;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @AfterEach
    public void afterEach() {
        this.roleRepository.deleteAll();
        this.privilegeRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_GetRoles_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        List<RoleDto> roleDtos = roleController.getRoles();

        assertEqualRoleList(List.of(role), roleDtos);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_GetRoleById_UnsuccessfulRoleNotFound() {
        Assertions.assertThrows(RoleNotFoundException.class,
                () -> roleController.getRoleById(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_GetRoleById_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        RoleDto roleDto = roleController.getRoleById(role.getId());

        assertExerciseDtoAndEntity(role, roleDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_AddRole_Success() {
        InputNewRole InputNewRole = createTestInputNewRole();

        RoleDto roleDto = roleController.addRole(InputNewRole);

        assertExerciseDtoAndInput(InputNewRole, roleDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_ModifyRole_UnsuccessfulRoleDoesNotExist() {
        InputRole inputRole = createTestInputRole(1L);

        Assertions.assertThrows(RoleNotFoundException.class,
                () -> roleController.modifyRole(inputRole)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_ModifyRole_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputRole inputRole = createTestInputRole(role.getId());

        RoleDto roleDto = roleController.modifyRole(inputRole);

        assertExerciseDtoAndInput(inputRole, roleDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_DeleteExercise_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(RoleNotFoundException.class,
                () -> roleController.deleteRole(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleController_DeleteRole_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));

        Long id = roleController.deleteRole(role.getId());

        Assertions.assertEquals(role.getId(), id);
    }

    private void assertEqualRoleList(
            List<RoleEntity> roleEntities,
            List<RoleDto> roleDtos
    ) {
        roleEntities.forEach(roleEntity -> assertExerciseDtoAndEntity(
                roleEntity,
                roleDtos.stream().filter(
                        roleDto -> Objects.equals(roleDto.getId(), roleEntity.getId())
                ).toList().getFirst())
        );
    }

    private void assertExerciseDtoAndEntity(RoleEntity roleEntity, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(roleEntity.getName(), roleDto.getName());
    }

    private void assertExerciseDtoAndInput(InputNewRole inputNewRole, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(inputNewRole.getName(), roleDto.getName());
    }
}