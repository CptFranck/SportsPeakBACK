package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
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

        Assertions.assertEquals(inputNewExercise.getName(), exerciseSaved.getName());
        Assertions.assertEquals(inputNewExercise.getDescription(), exerciseSaved.getDescription());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseSaved.getGoal());
        Assertions.assertEquals(inputNewExercise.getMuscleIds().size(), exerciseSaved.getMuscles().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseSaved.getExerciseTypes().size());
    }

    @Test
    void resolveInput_ValidInputExercise_ReturnExerciseEntity() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        InputExercise inputExercise = createTestInputExercise(exercise.getId());

        ExerciseEntity exerciseSaved = exerciseInputResolver.resolveInput(inputExercise);

        Assertions.assertEquals(inputExercise.getId(), exerciseSaved.getId());
        Assertions.assertEquals(inputExercise.getName(), exerciseSaved.getName());
        Assertions.assertEquals(inputExercise.getDescription(), exerciseSaved.getDescription());
        Assertions.assertEquals(inputExercise.getGoal(), exerciseSaved.getGoal());
        Assertions.assertEquals(inputExercise.getMuscleIds().size(), exerciseSaved.getMuscles().size());
        Assertions.assertEquals(inputExercise.getExerciseTypeIds().size(), exerciseSaved.getExerciseTypes().size());
        Assertions.assertEquals(exercise.getProgExercises().size(), exerciseSaved.getProgExercises().size());
    }
}
