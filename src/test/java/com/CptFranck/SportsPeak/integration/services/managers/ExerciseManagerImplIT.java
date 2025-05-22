package com.CptFranck.SportsPeak.integration.services.managers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.repository.MuscleRepository;
import com.CptFranck.SportsPeak.service.managerImpl.ExerciseManagerImpl;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.asserEqualExerciseType;
import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.assertEqualMuscle;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseManagerImplIT {

    @Autowired
    private ExerciseManagerImpl exerciseManager;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    private ExerciseEntity exercise;
    private ExerciseEntity exerciseBis;

    @BeforeEach
    public void setUp() {
        exercise = exerciseRepository.save(createTestExercise(null));
        exerciseBis = exerciseRepository.save(createTestExercise(null));
    }

    @AfterEach
    public void afterEach() {
        exerciseRepository.deleteAll();
        exerciseTypeRepository.deleteAll();
        muscleRepository.deleteAll();
    }

    @Test
    void saveExerciseType_AddNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(null);

        ExerciseTypeEntity exerciseTypeResolved = exerciseManager.saveExerciseType(exerciseType);

        asserEqualExerciseType(exerciseType, exerciseTypeResolved);
    }

    @Test
    void saveExerciseType_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);

        Assert.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseManager.saveExerciseType(exerciseType));
    }

    @Test
    void saveExerciseType_UpdateExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        exercise.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exercise);
        exerciseType.getExercises().add(exerciseBis);

        ExerciseTypeEntity exerciseTypeResolved = exerciseManager.saveExerciseType(exerciseType);

        exerciseType = exerciseTypeRepository.findById(exerciseType.getId()).orElseThrow();
        asserEqualExerciseType(exerciseType, exerciseTypeResolved);
        Assertions.assertTrue(exerciseTypeResolved.getExercises().stream().noneMatch(e -> Objects.equals(exercise.getId(), e.getId())));
        Assertions.assertTrue(exerciseTypeResolved.getExercises().stream().anyMatch(e -> Objects.equals(exerciseBis.getId(), e.getId())));
    }

    @Test
    void saveMuscle_AddNewMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = createTestMuscle(null);

        MuscleEntity muscleResolved = exerciseManager.saveMuscle(muscle);

        assertEqualMuscle(muscle, muscleResolved);
    }

    @Test
    void saveMuscle_InvalidMuscleId_ThrowMuscleNotFoundException() {
        MuscleEntity muscle = createTestMuscle(1L);

        Assert.assertThrows(MuscleNotFoundException.class, () -> exerciseManager.saveMuscle(muscle));
    }

    @Test
    void saveMuscle_UpdateMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);
        muscle.getExercises().add(exerciseBis);

        MuscleEntity muscleResolved = exerciseManager.saveMuscle(muscle);

        muscle = muscleRepository.findById(muscle.getId()).orElseThrow();
        assertEqualMuscle(muscle, muscleResolved);
        Assertions.assertTrue(muscleResolved.getExercises().stream().noneMatch(e -> Objects.equals(exercise.getId(), e.getId())));
        Assertions.assertTrue(muscleResolved.getExercises().stream().anyMatch(e -> Objects.equals(exerciseBis.getId(), e.getId())));
    }
}
