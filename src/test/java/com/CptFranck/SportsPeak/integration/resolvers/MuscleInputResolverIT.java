package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.resolvers.MuscleInputResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class MuscleInputResolverIT {

    @Autowired
    private MuscleInputResolver muscleInputResolver;

    @Autowired
    private MuscleRepository muscleRepository;

    @Test
    void resolveInput_ValidInputNewExerciseType_ReturnMuscleEntity() {
        InputNewMuscle inputNewMuscle = createTestInputNewMuscle();

        MuscleEntity muscleSaved = muscleInputResolver.resolveInput(inputNewMuscle);

        assertMuscleInputAndEntity(inputNewMuscle, muscleSaved);
    }

    @Test
    void resolveInput_InvalidMuscleId_ThrowMuscleNotFoundException() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        InputMuscle inputMuscle = createTestInputMuscle(muscle.getId() + 1);

        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleInputResolver.resolveInput(inputMuscle));
    }

    @Test
    void resolveInput_ValidInputExerciseType_ReturnMuscleEntity() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        InputMuscle inputMuscle = createTestInputMuscle(muscle.getId());

        MuscleEntity muscleSaved = muscleInputResolver.resolveInput(inputMuscle);

        Assertions.assertEquals(inputMuscle.getId(), muscleSaved.getId());
        assertMuscleInputAndEntity(inputMuscle, muscleSaved);
    }

    private void assertMuscleInputAndEntity(InputNewMuscle expectedMuscle, MuscleEntity actualMuscle) {
        Assertions.assertEquals(expectedMuscle.getName(), actualMuscle.getName());
        Assertions.assertEquals(expectedMuscle.getFunction(), actualMuscle.getFunction());
        Assertions.assertEquals(expectedMuscle.getDescription(), actualMuscle.getDescription());
        Assertions.assertEquals(expectedMuscle.getExerciseIds().size(), actualMuscle.getExercises().size());
    }
}
