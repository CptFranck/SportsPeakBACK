package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.controller.ExerciseTypeController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseTypeControllerIntTest {

    @Autowired
    private ExerciseTypeController exerciseTypeController;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @AfterEach
    void afterEach() {
        exerciseTypeRepository.deleteAll();
    }

    @Test
    void ExerciseTypeController_GetExerciseTypes_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        List<ExerciseTypeDto> exerciseTypeDtos = exerciseTypeController.getExerciseTypes();

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeDtos);
    }


    @Test
    void ExerciseTypeController_GetExerciseTypeById_Unsuccessful() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.getExerciseTypeById(1L)
        );
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.getExerciseTypeById(exerciseType.getId());

        assertExerciseDtoAndEntity(exerciseType, exerciseTypeDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_AddExerciseType_Success() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(inputNewExerciseType);

        assertExerciseDtoAndInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_ModifyExerciseType_UnsuccessfulExerciseTypeNotFound() {
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(1L);

        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.addExerciseType(inputNewExerciseType)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_ModifyExerciseType_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(exerciseType.getId());

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(inputNewExerciseType);

        assertExerciseDtoAndInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_DeleteExerciseType_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class,
                () -> exerciseTypeController.deleteExerciseType(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseTypeController_DeleteExercise_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        Long id = exerciseTypeController.deleteExerciseType(exerciseType.getId());

        Assertions.assertEquals(exerciseType.getId(), id);
    }


    private void assertEqualExerciseList(List<ExerciseTypeEntity> exerciseTypeEntities, List<ExerciseTypeDto> exerciseTypeDtos) {
        exerciseTypeDtos.forEach(exerciseTypeDto -> assertExerciseDtoAndEntity(
                exerciseTypeEntities.stream().filter(
                        exerciseTypeEntity -> Objects.equals(exerciseTypeEntity.getId(), exerciseTypeDto.getId())
                ).toList().getFirst(),
                exerciseTypeDto)
        );
    }

    private void assertExerciseDtoAndEntity(ExerciseTypeEntity exerciseType, ExerciseTypeDto exerciseTypeDto) {
        Assertions.assertNotNull(exerciseTypeDto);
        Assertions.assertEquals(exerciseType.getId(), exerciseTypeDto.getId());
        Assertions.assertEquals(exerciseType.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(exerciseType.getGoal(), exerciseTypeDto.getGoal());
    }

    private void assertExerciseDtoAndInput(InputNewExerciseType inputNewExerciseType, ExerciseTypeDto exerciseTypeDto) {
        Assertions.assertNotNull(exerciseTypeDto);
        Assertions.assertEquals(inputNewExerciseType.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(inputNewExerciseType.getGoal(), exerciseTypeDto.getGoal());
    }
}