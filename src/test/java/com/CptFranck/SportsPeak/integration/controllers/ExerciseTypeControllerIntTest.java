package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.ExerciseTypeController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.*;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseTypeControllerIntTest {

    @Autowired
    private ExerciseTypeController exerciseTypeController;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    private ExerciseTypeEntity exerciseType;

    @BeforeEach
    void setUp() {
        exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

    }

    @AfterEach
    void afterEach() {
        exerciseTypeRepository.deleteAll();
    }

    @Test
    void ExerciseTypeController_GetExerciseTypes_Success() {
        List<ExerciseTypeDto> exerciseTypeDtos = exerciseTypeController.getExerciseTypes();

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeDtos);
    }


    @Test
    void ExerciseTypeController_GetExerciseTypeById_UnsuccessfulExerciseTypeNotFound() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.getExerciseTypeById(exerciseType.getId() + 1)
        );
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Success() {
        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.getExerciseTypeById(exerciseType.getId());

        assertExerciseTypeDtoAndEntity(exerciseType, exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_AddExerciseType_UnsuccessfulNotAuthenticated() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseTypeController.addExerciseType(inputNewExerciseType)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_AddExerciseType_Success() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(inputNewExerciseType);

        assertExerciseTypeDtoAndInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_ModifyExerciseType_UnsuccessfulNotAuthenticated() {
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(exerciseType.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseTypeController.addExerciseType(inputNewExerciseType)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_ModifyExerciseType_UnsuccessfulExerciseTypeNotFound() {
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(exerciseType.getId() + 1);

        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.addExerciseType(inputNewExerciseType)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_ModifyExerciseType_Success() {
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(exerciseType.getId());

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(inputNewExerciseType);

        assertExerciseTypeDtoAndInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_DeleteExerciseType_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseTypeController.deleteExerciseType(exerciseType.getId())
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_DeleteExerciseType_UnsuccessfulExerciseTypeNotFound() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.deleteExerciseType(exerciseType.getId() + 1)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_DeleteExercise_Success() {
        Long id = exerciseTypeController.deleteExerciseType(exerciseType.getId());

        Assertions.assertEquals(exerciseType.getId(), id);
    }


    private void assertEqualExerciseList(List<ExerciseTypeEntity> exerciseTypeEntities, List<ExerciseTypeDto> exerciseTypeDtos) {
        Assertions.assertEquals(exerciseTypeEntities.size(), exerciseTypeDtos.size());
        exerciseTypeDtos.forEach(exerciseTypeDto -> assertExerciseTypeDtoAndEntity(
                exerciseTypeEntities.stream().filter(
                        exerciseTypeEntity -> Objects.equals(exerciseTypeEntity.getId(), exerciseTypeDto.getId())
                ).toList().getFirst(),
                exerciseTypeDto)
        );
    }

    private void assertExerciseTypeDtoAndEntity(ExerciseTypeEntity exerciseType, ExerciseTypeDto exerciseTypeDto) {
        Assertions.assertNotNull(exerciseTypeDto);
        Assertions.assertEquals(exerciseType.getId(), exerciseTypeDto.getId());
        Assertions.assertEquals(exerciseType.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(exerciseType.getGoal(), exerciseTypeDto.getGoal());
        Assertions.assertEquals(exerciseType.getExercises().size(), exerciseTypeDto.getExercises().size());
    }

    private void assertExerciseTypeDtoAndInput(InputNewExerciseType inputNewExerciseType, ExerciseTypeDto exerciseTypeDto) {
        Assertions.assertNotNull(exerciseTypeDto);
        Assertions.assertEquals(inputNewExerciseType.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(inputNewExerciseType.getGoal(), exerciseTypeDto.getGoal());
        Assertions.assertEquals(inputNewExerciseType.getExerciseIds().size(), exerciseTypeDto.getExercises().size());
    }
}