package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.ExerciseTypeController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeStillUsedInExerciseException;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
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
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseTypeControllerIT {

    @Autowired
    private ExerciseTypeController exerciseTypeController;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private ExerciseTypeEntity exerciseType;

    @BeforeEach
    void setUp() {
        exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
    }

    @AfterEach
    void afterEach() {
        exerciseRepository.deleteAll();
        exerciseTypeRepository.deleteAll();
    }

    @Test
    void getExerciseTypes_ValidUse_ReturnListOfExerciseTypeDto() {
        List<ExerciseTypeDto> exerciseTypeDtos = exerciseTypeController.getExerciseTypes();

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeDtos);
    }


    @Test
    void getExerciseTypeById_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.getExerciseTypeById(exerciseType.getId() + 1));
    }

    @Test
    void getExerciseTypeById_ValidInput_ReturnExerciseTypeDto() {
        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.getExerciseTypeById(exerciseType.getId());

        assertExerciseTypeDtoAndEntity(exerciseType, exerciseTypeDto);
    }

    @Test
    void addExerciseType_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseTypeController.addExerciseType(inputNewExerciseType));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addExerciseType_ValidInput_ReturnExerciseTypeDto() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(inputNewExerciseType);

        assertExerciseTypeDtoAndInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void modifyExerciseType_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        InputExerciseType inputExerciseType = createTestInputExerciseType(exerciseType.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseTypeController.modifyExerciseType(inputExerciseType));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyExerciseType_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        InputExerciseType inputExerciseType = createTestInputExerciseType(exerciseType.getId() + 1);

        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.modifyExerciseType(inputExerciseType));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyExerciseType_ValidInput_ReturnExerciseTypeDto() {
        InputExerciseType inputExerciseType = createTestInputExerciseType(exerciseType.getId());

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.modifyExerciseType(inputExerciseType);

        assertExerciseTypeDtoAndInput(inputExerciseType, exerciseTypeDto);
    }

    @Test
    void deleteExerciseType_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> exerciseTypeController.deleteExerciseType(exerciseType.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteExerciseType_ExerciseTypeStillUsedByExercise_ThrowExerciseTypeStillUsedInExerciseException() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exercise);

        Assertions.assertThrows(ExerciseTypeStillUsedInExerciseException.class, () -> exerciseTypeController.deleteExerciseType(exerciseType.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteExerciseType_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.deleteExerciseType(exerciseType.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteExerciseType_ValidInput_Void() {
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