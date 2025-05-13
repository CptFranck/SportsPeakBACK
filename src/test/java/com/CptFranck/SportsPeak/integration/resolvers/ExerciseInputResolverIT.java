package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.resolvers.ExerciseInputResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseInputResolverIT {

    @Autowired
    private ExerciseInputResolver exerciseInputResolver;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void resolveInput_ValidInputNewExercise_ReturnExerciseEntity() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        ExerciseEntity exerciseSaved = exerciseInputResolver.resolveInput(inputNewExercise);

        assertExerciseInputAndEntity(inputNewExercise, exerciseSaved);
    }

    @Test
    void resolveInput_InvalidExerciseId_ReturnExerciseEntity() {
        InputExercise inputExercise = createTestInputExercise(1L);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseInputResolver.resolveInput(inputExercise));
    }

    @Test
    void resolveInput_ValidInputExercise_ReturnExerciseEntity() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        InputExercise inputExercise = createTestInputExercise(exercise.getId());

        ExerciseEntity exerciseSaved = exerciseInputResolver.resolveInput(inputExercise);

        Assertions.assertEquals(inputExercise.getId(), exerciseSaved.getId());
        assertExerciseInputAndEntity(inputExercise, exerciseSaved);
        Assertions.assertEquals(exercise.getProgExercises().size(), exerciseSaved.getProgExercises().size());
    }

    private void assertExerciseInputAndEntity(InputNewExercise expectedExercise, ExerciseEntity actualExercise) {
        Assertions.assertEquals(expectedExercise.getName(), actualExercise.getName());
        Assertions.assertEquals(expectedExercise.getDescription(), actualExercise.getDescription());
        Assertions.assertEquals(expectedExercise.getGoal(), actualExercise.getGoal());
        Assertions.assertEquals(expectedExercise.getMuscleIds().size(), actualExercise.getMuscles().size());
        Assertions.assertEquals(expectedExercise.getExerciseTypeIds().size(), actualExercise.getExerciseTypes().size());
    }
}
