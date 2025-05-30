package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.ExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
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

import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseControllerIT {

    @Autowired
    private ExerciseController exerciseController;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private ExerciseEntity exercise;

    @BeforeEach
    void setUp() {
        exercise = exerciseRepository.save(createTestExercise(null));
    }

    @AfterEach
    void afterEach() {
        exerciseRepository.deleteAll();
    }

    @Test
    void getExercises_ValidUse_ReturnListOfExerciseDto() {
        List<ExerciseDto> exerciseDtos = exerciseController.getExercises();

        assertEqualExerciseList(List.of(exercise), exerciseDtos);
    }

    @Test
    void getExerciseById_InvalidExerciseId_ThrowExerciseNotFoundException() {
        exerciseRepository.delete(exercise);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseController.getExerciseById(exercise.getId()));
    }

    @Test
    void getExerciseById_ValidInput_ReturnExerciseDto() {
        ExerciseDto exerciseDto = exerciseController.getExerciseById(exercise.getId());

        assertExerciseDtoAndEntity(exercise, exerciseDto);
    }

    @Test
    void addExercise_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseController.addExercise(inputNewExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addExercise_ValidInput_ReturnExerciseDto() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        ExerciseDto exerciseDto = exerciseController.addExercise(inputNewExercise);

        assertExerciseDtoAndInput(inputNewExercise, exerciseDto);
    }

    @Test
    void modifyExercise_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputExercise inputExercise = createTestInputExercise(exercise.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseController.modifyExercise(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyExercise_InvalidExerciseId_ThrowExerciseNotFoundException() {
        exerciseRepository.delete(exercise);

        InputExercise inputExercise = createTestInputExercise(exercise.getId());

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.modifyExercise(inputExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyExercise_ValidInput_ReturnExerciseDto() {
        InputExercise inputExercise = createTestInputExercise(exercise.getId());

        ExerciseDto exerciseDto = exerciseController.modifyExercise(inputExercise);

        assertExerciseDtoAndInput(inputExercise, exerciseDto);
    }

    @Test
    void deleteExercise_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseController.deleteExercise(exercise.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteExercise_InvalidExerciseId_ThrowExerciseNotFoundException() {
        exerciseRepository.delete(exercise);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseController.deleteExercise(exercise.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteExercise_ValidInput_ReturnExerciseId() {
        Long id = exerciseController.deleteExercise(exercise.getId());

        Assertions.assertEquals(exercise.getId(), id);
    }

    private void assertEqualExerciseList(
            List<ExerciseEntity> exerciseEntities,
            List<ExerciseDto> exerciseDtos
    ) {
        Assertions.assertEquals(exerciseEntities.size(), exerciseDtos.size());
        exerciseDtos.forEach(exerciseDto -> assertExerciseDtoAndEntity(
                exerciseEntities.stream().filter(
                        exerciseEntity -> Objects.equals(exerciseEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }
}