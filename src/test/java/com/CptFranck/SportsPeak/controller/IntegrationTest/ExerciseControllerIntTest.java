package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.controller.ExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseControllerIntTest {

    @Autowired
    private ExerciseController exerciseController;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @AfterEach
    void afterEach() {
        exerciseRepository.deleteAll();
    }

    @Test
    void ExerciseController_GetExercises_Success() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));

        List<ExerciseDto> exerciseDtos = exerciseController.getExercises();

        assertEqualExerciseList(List.of(exercise), exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.getExerciseById(1L)
        );
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));

        ExerciseDto exerciseDto = exerciseController.getExerciseById(exercise.getId());

        assertExerciseDtoAndEntity(exercise, exerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseController_AddExercise_Success() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        ExerciseDto exerciseDto = exerciseController.addExercise(inputNewExercise);

        assertExerciseDtoAndInput(inputNewExercise, exerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseController_ModifyExercise_UnsuccessfulExerciseNotFound() {
        InputExercise inputExercise = createTestInputExercise(1L);

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.modifyExercise(inputExercise)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseController_ModifyExercise_Success() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        InputExercise inputExercise = createTestInputExercise(exercise.getId());

        ExerciseDto exerciseDto = exerciseController.modifyExercise(inputExercise);

        assertExerciseDtoAndInput(inputExercise, exerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseController_DeleteExercise_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.deleteExercise(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ExerciseController_DeleteExercise_Success() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));

        Long id = exerciseController.deleteExercise(exercise.getId());

        Assertions.assertEquals(exercise.getId(), id);
    }

    private void assertEqualExerciseList(
            List<ExerciseEntity> exerciseEntities,
            List<ExerciseDto> exerciseDtos
    ) {
        exerciseDtos.forEach(exerciseDto -> assertExerciseDtoAndEntity(
                exerciseEntities.stream().filter(
                        exerciseEntity -> Objects.equals(exerciseEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }

    private void assertExerciseDtoAndEntity(ExerciseEntity exerciseEntity, ExerciseDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(exerciseEntity.getName(), exerciseDto.getName());
        Assertions.assertEquals(exerciseEntity.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(exerciseEntity.getDescription(), exerciseDto.getDescription());
        Assertions.assertEquals(exerciseEntity.getMuscles().size(), exerciseDto.getMuscles().size());
        Assertions.assertEquals(exerciseEntity.getExerciseTypes().size(), exerciseDto.getProgExercises().size());
    }

    private void assertExerciseDtoAndInput(InputNewExercise inputNewExercise, ExerciseDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(inputNewExercise.getName(), exerciseDto.getName());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(inputNewExercise.getDescription(), exerciseDto.getDescription());
        Assertions.assertEquals(inputNewExercise.getMuscleIds().size(), exerciseDto.getMuscles().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseDto.getProgExercises().size());
    }
}