package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.controller.PrivilegeController;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PrivilegeControllerIntTest {

    @Autowired
    private PrivilegeController privilegeController;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @AfterEach
    public void afterEach() {
        this.privilegeRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_GetPrivileges_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));

        List<PrivilegeDto> PerformanceLogDtos = privilegeController.getPrivileges();

        assertEqualExerciseList(List.of(privilege), PerformanceLogDtos);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_GetPrivilegeById_Unsuccessful() {
        Assertions.assertThrows(PrivilegeNotFoundException.class,
                () -> privilegeController.getPrivilegeById(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_GetPrivilegeById_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));

        PrivilegeDto PerformanceLogDto = privilegeController.getPrivilegeById(privilege.getId());

        assertExerciseDtoAndEntity(privilege, PerformanceLogDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_AddPrivilege_Success() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();

        PrivilegeDto exerciseDto = privilegeController.addPrivilege(inputNewPrivilege);

        assertExerciseDtoAndInput(inputNewPrivilege, exerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_ModifyPrivilege_UnsuccessfulDoesNotExist() {
        InputPrivilege inputExercise = createTestInputPrivilege(1L);

        Assertions.assertThrows(PrivilegeNotFoundException.class,
                () -> privilegeController.modifyPrivilege(inputExercise)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_ModifyPrivilege_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));
        InputPrivilege inputExercise = createTestInputPrivilege(privilege.getId());

        PrivilegeDto exerciseDto = privilegeController.modifyPrivilege(inputExercise);

        assertExerciseDtoAndInput(inputExercise, exerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_DeletePrivilege_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(PrivilegeNotFoundException.class,
                () -> privilegeController.deletePrivilege(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeController_DeletePrivilege_Success() {
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));

        Long id = privilegeController.deletePrivilege(privilege.getId());

        Assertions.assertEquals(privilege.getId(), id);
    }

    private void assertEqualExerciseList(
            List<PrivilegeEntity> privilegeEntities,
            List<PrivilegeDto> privilegeDtos
    ) {
        privilegeDtos.forEach(exerciseDto -> assertExerciseDtoAndEntity(
                privilegeEntities.stream().filter(
                        privilegeEntity -> Objects.equals(privilegeEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }

    private void assertExerciseDtoAndEntity(PrivilegeEntity privilegeEntity, PrivilegeDto privilegeDto) {
        Assertions.assertNotNull(privilegeDto);
        Assertions.assertEquals(privilegeEntity.getId(), privilegeDto.getId());
        Assertions.assertEquals(privilegeEntity.getName(), privilegeDto.getName());
    }

    private void assertExerciseDtoAndInput(InputNewPrivilege inputNewPrivilege, PrivilegeDto privilegeDto) {
        Assertions.assertNotNull(privilegeDto);
        Assertions.assertEquals(inputNewPrivilege.getName(), privilegeDto.getName());
    }
}