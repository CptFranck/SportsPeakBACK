package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.MuscleController;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.MuscleRepository;
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

import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.*;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class MuscleControllerIT {

    @Autowired
    private MuscleController muscleController;

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private MuscleEntity muscle;

    @BeforeEach
    void setUp() {
        muscle = muscleRepository.save(createTestMuscle(null));
    }

    @AfterEach
    void afterEach() {
        exerciseRepository.deleteAll();
        muscleRepository.deleteAll();
    }

    @Test
    void getMuscles_ValidUse_ReturnListOfMuscleDto() {
        List<MuscleDto> muscleDtos = muscleController.getMuscles();

        assertEqualMuscleList(List.of(muscle), muscleDtos);
    }

    @Test
    void getMuscleById_InvalidMuscleId_ThrowMuscleNotFoundException() {
        muscleRepository.delete(muscle);

        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleController.getMuscleById(muscle.getId()));
    }

    @Test
    void getMuscleById_ValidMuscleId_ReturnMuscleDto() {
        MuscleDto muscleDto = muscleController.getMuscleById(muscle.getId());

        assertMuscleDtoAndEntity(muscle, muscleDto);
    }

    @Test
    void addMuscle_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputNewMuscle inputNewExercise = createTestInputNewMuscle();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> muscleController.addMuscle(inputNewExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addMuscle_ValidInput_ReturnMuscleDto() {
        InputNewMuscle inputNewExercise = createTestInputNewMuscle();

        MuscleDto muscleDto = muscleController.addMuscle(inputNewExercise);

        assertMuscleDtoAndInput(inputNewExercise, muscleDto);
    }

    @Test
    void modifyMuscle_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputMuscle inputMuscle = createTestInputMuscle(muscle.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> muscleController.modifyMuscle(inputMuscle));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyMuscle_InvalidMuscleId_ThrowMuscleNotFoundException() {
        InputMuscle inputMuscle = createTestInputMuscle(muscle.getId());
        muscleRepository.delete(muscle);

        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleController.modifyMuscle(inputMuscle));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyMuscle_ValidInput_ReturnMuscleDto() {
        InputMuscle testInputMuscle = createTestInputMuscle(muscle.getId());

        MuscleDto exerciseDto = muscleController.modifyMuscle(testInputMuscle);

        assertMuscleDtoAndInput(testInputMuscle, exerciseDto);
    }

    @Test
    void deleteMuscle_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> muscleController.deleteMuscle(muscle.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteMuscle_MuscleStillUsedByExercise_ThrowMuscleStillUsedInExerciseException() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);

        Assertions.assertThrows(MuscleStillUsedInExerciseException.class, () -> muscleController.deleteMuscle(muscle.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteMuscle_InvalidMuscleId_ThrowMuscleNotFoundException() {
        muscleRepository.delete(muscle);

        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleController.deleteMuscle(muscle.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteMuscle_ValidInput_ReturnMuscleId() {
        Long id = muscleController.deleteMuscle(muscle.getId());

        Assertions.assertEquals(muscle.getId(), id);
    }

    private void assertEqualMuscleList(
            List<MuscleEntity> muscleEntities,
            List<MuscleDto> muscleDtos
    ) {
        Assertions.assertEquals(muscleEntities.size(), muscleDtos.size());
        muscleDtos.forEach(muscleDto -> assertMuscleDtoAndEntity(
                muscleEntities.stream().filter(
                        muscleEntity -> Objects.equals(muscleEntity.getId(), muscleDto.getId())
                ).toList().getFirst(),
                muscleDto)
        );
    }


}