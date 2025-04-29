package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.PrivilegeController;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
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

import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.*;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PrivilegeControllerIntTest {

    @Autowired
    private PrivilegeController privilegeController;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    private PrivilegeEntity privilege;

    @BeforeEach
    void setUp() {
        privilege = privilegeRepository.save(createTestPrivilege(null, 0));
    }

    @AfterEach
    public void afterEach() {
        this.privilegeRepository.deleteAll();
    }

    @Test
    void PrivilegeController_GetPrivileges_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.getPrivileges());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_GetPrivileges_Success() {
        List<PrivilegeDto> PerformanceLogDtos = privilegeController.getPrivileges();

        assertEqualExerciseList(List.of(privilege), PerformanceLogDtos);
    }

    @Test
    void PrivilegeController_GetPrivilegeById_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.getPrivilegeById(privilege.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_GetPrivilegeById_UnsuccessfulPrivilegeNotFound() {
        Assertions.assertThrows(PrivilegeNotFoundException.class, () -> privilegeController.getPrivilegeById(privilege.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_GetPrivilegeById_Success() {
        PrivilegeDto performanceLogDto = privilegeController.getPrivilegeById(privilege.getId());

        assertPrivilegeDtoAndEntity(privilege, performanceLogDto);
    }

    @Test
    void PrivilegeController_AddPrivilege_UnsuccessfulNotAuthenticated() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.addPrivilege(inputNewPrivilege));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_AddPrivilege_Success() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();

        PrivilegeDto exerciseDto = privilegeController.addPrivilege(inputNewPrivilege);

        assertPrivilegeDtoAndInput(inputNewPrivilege, exerciseDto);
    }


    @Test
    void PrivilegeController_ModifyPrivilege_UnsuccessfulNotAuthenticated() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.modifyPrivilege(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_ModifyPrivilege_UnsuccessfulDoesNotExist() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId() + 1);

        Assertions.assertThrows(PrivilegeNotFoundException.class,
                () -> privilegeController.modifyPrivilege(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_ModifyPrivilege_Success() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId());

        PrivilegeDto exerciseDto = privilegeController.modifyPrivilege(inputExercise);

        assertPrivilegeDtoAndInput(inputExercise, exerciseDto);
    }

    @Test
    void PrivilegeController_DeletePrivilege_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.deletePrivilege(privilege.getId()));
    }

//    @Test
//    @WithMockUser(username = "user", roles = "ADMIN")
//    void PrivilegeController_DeletePrivilege_UnsuccessfulExerciseNotFound() {
//        Assertions.assertThrows(PrivilegeNotFoundException.class,
//                () -> privilegeController.deletePrivilege(privilege.getId() + 1));
//    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_DeletePrivilege_Success() {
        Long id = privilegeController.deletePrivilege(privilege.getId());

        Assertions.assertEquals(privilege.getId(), id);
    }

    private void assertEqualExerciseList(
            List<PrivilegeEntity> privilegeEntities,
            List<PrivilegeDto> privilegeDtos
    ) {
        Assertions.assertEquals(privilegeEntities.size(), privilegeDtos.size());
        privilegeDtos.forEach(exerciseDto -> assertPrivilegeDtoAndEntity(
                privilegeEntities.stream().filter(
                        privilegeEntity -> Objects.equals(privilegeEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }

    private void assertPrivilegeDtoAndEntity(PrivilegeEntity privilegeEntity, PrivilegeDto privilegeDto) {
        Assertions.assertNotNull(privilegeDto);
        Assertions.assertEquals(privilegeEntity.getId(), privilegeDto.getId());
        Assertions.assertEquals(privilegeEntity.getName(), privilegeDto.getName());
    }

    private void assertPrivilegeDtoAndInput(InputNewPrivilege inputNewPrivilege, PrivilegeDto privilegeDto) {
        Assertions.assertNotNull(privilegeDto);
        Assertions.assertEquals(inputNewPrivilege.getName(), privilegeDto.getName());
    }
}