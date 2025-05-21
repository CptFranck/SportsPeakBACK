package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.resolver.ExerciseTypeInputResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestInputExerciseType;
import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestInputNewExerciseType;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseTypeInputResolverIT {

    @Autowired
    private ExerciseTypeInputResolver exerciseTypeInputResolver;

    @Test
    void resolveInput_ValidInputNewExerciseType_ReturnExerciseTypeEntity() {
        InputNewExerciseType inputNewExercise = createTestInputNewExerciseType();

        ExerciseTypeEntity exerciseSaved = exerciseTypeInputResolver.resolveInput(inputNewExercise);

        assertExerciseTypeInputAndEntity(inputNewExercise, exerciseSaved);
    }

    @Test
    void resolveInput_ValidInputExerciseType_ReturnExerciseTypeEntity() {
        InputExerciseType inputExerciseType = createTestInputExerciseType(1L);

        ExerciseTypeEntity exerciseSaved = exerciseTypeInputResolver.resolveInput(inputExerciseType);

        Assertions.assertEquals(inputExerciseType.getId(), exerciseSaved.getId());
        assertExerciseTypeInputAndEntity(inputExerciseType, exerciseSaved);
    }

    private void assertExerciseTypeInputAndEntity(InputNewExerciseType expectedExerciseType, ExerciseTypeEntity actualExerciseType) {
        Assertions.assertEquals(expectedExerciseType.getName(), actualExerciseType.getName());
        Assertions.assertEquals(expectedExerciseType.getGoal(), actualExerciseType.getGoal());
        Assertions.assertEquals(expectedExerciseType.getExerciseIds().size(), actualExerciseType.getExercises().size());
    }
}
