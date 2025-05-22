package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.PrivilegeController;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.repository.PrivilegeRepository;
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

import static com.CptFranck.SportsPeak.utils.PrivilegeTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PrivilegeControllerIT {

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
    void getPrivileges_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.getPrivileges());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getPrivileges_ValidUse_ReturnListOfPrivilegeDto() {
        List<PrivilegeDto> PerformanceLogDtos = privilegeController.getPrivileges();

        assertEqualExerciseList(List.of(privilege), PerformanceLogDtos);
    }

    @Test
    void getPrivilegeById_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.getPrivilegeById(privilege.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getPrivilegeById_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        privilegeRepository.delete(privilege);

        Assertions.assertThrows(PrivilegeNotFoundException.class, () -> privilegeController.getPrivilegeById(privilege.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getPrivilegeById_ValidPrivilegeId_ReturnPrivilegeDto() {
        PrivilegeDto performanceLogDto = privilegeController.getPrivilegeById(privilege.getId());

        assertPrivilegeDtoAndEntity(privilege, performanceLogDto);
    }

    @Test
    void addPrivilege_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.addPrivilege(inputNewPrivilege));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addPrivilege_AddNewPrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();

        assertThrows(PrivilegeExistsException.class, () -> privilegeController.addPrivilege(inputNewPrivilege));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addPrivilege_AddNewPrivilege_ReturnPrivilegeDto() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();
        inputNewPrivilege.setName("name");
        PrivilegeDto exerciseDto = privilegeController.addPrivilege(inputNewPrivilege);

        assertPrivilegeDtoAndInput(inputNewPrivilege, exerciseDto);
    }

    @Test
    void modifyPrivilege_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.modifyPrivilege(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyPrivilege_UpdatePrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId() + 1);

        Assertions.assertThrows(PrivilegeNotFoundException.class, () -> privilegeController.modifyPrivilege(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyPrivilege_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId() + 1);
        privilege.setName("name");
        privilegeRepository.save(privilege);

        Assertions.assertThrows(PrivilegeNotFoundException.class, () -> privilegeController.modifyPrivilege(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyPrivilege_UpdatePrivilege_ReturnPrivilegeDto() {
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId());

        PrivilegeDto exerciseDto = privilegeController.modifyPrivilege(inputExercise);

        assertPrivilegeDtoAndInput(inputExercise, exerciseDto);
    }

    @Test
    void deletePrivilege_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> privilegeController.deletePrivilege(privilege.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deletePrivilege_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        privilegeRepository.delete(privilege);

        Assertions.assertThrows(PrivilegeNotFoundException.class, () -> privilegeController.deletePrivilege(privilege.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deletePrivilege_ValidInput_Void() {
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
}