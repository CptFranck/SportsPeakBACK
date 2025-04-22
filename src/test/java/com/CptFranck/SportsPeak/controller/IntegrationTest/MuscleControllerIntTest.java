package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.controller.MuscleController;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class MuscleControllerIntTest {

    @Autowired
    private MuscleController muscleController;

    @Autowired
    private MuscleRepository muscleRepository;

    @AfterEach
    void afterEach() {
        muscleRepository.deleteAll();
    }

    @Test
    void MuscleController_GetMuscles_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));

        List<MuscleDto> muscleDtos = muscleController.getMuscles();

        assertEqualExerciseList(List.of(muscle), muscleDtos);
    }

    @Test
    void MuscleController_GetMuscleById_Unsuccessful() {
        Assertions.assertThrows(MuscleNotFoundException.class,
                () -> muscleController.getMuscleById(1L)
        );
    }

    @Test
    void MuscleController_GetMuscleById_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));

        MuscleDto muscleDto = muscleController.getMuscleById(muscle.getId());

        assertExerciseDtoAndEntity(muscle, muscleDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void MuscleController_AddMuscle_Success() {
        InputNewMuscle inputNewExercise = createTestInputNewMuscle();

        MuscleDto muscleDto = muscleController.addMuscle(inputNewExercise);

        assertExerciseDtoAndInput(inputNewExercise, muscleDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void MuscleController_ModifyMuscle_UnsuccessfulDoesNotExist() {
        InputMuscle inputMuscle = createTestInputMuscle(1L);

        Assertions.assertThrows(MuscleNotFoundException.class,
                () -> muscleController.modifyMuscle(inputMuscle)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void MuscleController_ModifyMuscle_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        InputMuscle testInputMuscle = createTestInputMuscle(muscle.getId());

        MuscleDto exerciseDto = muscleController.modifyMuscle(testInputMuscle);

        assertExerciseDtoAndInput(testInputMuscle, exerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void MuscleController_DeleteMuscle_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(MuscleNotFoundException.class,
                () -> muscleController.deleteMuscle(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void MuscleController_DeleteMuscle_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));

        Long id = muscleController.deleteMuscle(muscle.getId());

        Assertions.assertEquals(muscle.getId(), id);
    }

    private void assertEqualExerciseList(
            List<MuscleEntity> muscleEntities,
            List<MuscleDto> muscleDtos
    ) {
        muscleDtos.forEach(muscleDto -> assertExerciseDtoAndEntity(
                muscleEntities.stream().filter(
                        muscleEntity -> Objects.equals(muscleEntity.getId(), muscleDto.getId())
                ).toList().getFirst(),
                muscleDto)
        );
    }

    private void assertExerciseDtoAndEntity(MuscleEntity muscleEntity, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(muscleEntity.getId(), muscleDto.getId());
        Assertions.assertEquals(muscleEntity.getName(), muscleDto.getName());
        Assertions.assertEquals(muscleEntity.getLatinName(), muscleDto.getLatinName());
        Assertions.assertEquals(muscleEntity.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(muscleEntity.getDescription(), muscleDto.getDescription());
    }

    private void assertExerciseDtoAndInput(InputNewMuscle inputNewExercise, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(inputNewExercise.getName(), muscleDto.getName());
        Assertions.assertEquals(inputNewExercise.getLatinName(), muscleDto.getLatinName());
        Assertions.assertEquals(inputNewExercise.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(inputNewExercise.getDescription(), muscleDto.getDescription());
    }
}