package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.repository.*;
import com.CptFranck.SportsPeak.service.serviceImpl.ExerciseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.assertEqualExercise;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseServiceImplIT {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private ExerciseServiceImpl exerciseServiceImpl;

    private ExerciseEntity exercise;
    private ExerciseEntity exerciseBis;

    @BeforeEach
    public void setUp() {
        exercise = exerciseRepository.save(createTestExercise(null));
        exerciseBis = exerciseRepository.save(createTestExercise(null));
    }

    @AfterEach
    public void afterEach() {
        this.progExerciseRepository.deleteAll();
        this.userRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.muscleRepository.deleteAll();
        this.exerciseTypeRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfExerciseEntity() {
        List<ExerciseEntity> exercisesFound = exerciseServiceImpl.findAll();

        assertEqualExerciseList(List.of(exercise, exerciseBis), exercisesFound.stream().toList());
    }

    @Test
    void findOne_InvalidExerciseId_ThrowExerciseNotFoundException() {
        exerciseRepository.delete(exercise);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.findOne(exercise.getId()));
    }

    @Test
    void findOne_ValidExerciseId_ReturnExerciseEntity() {
        ExerciseEntity exerciseFound = exerciseServiceImpl.findOne(exercise.getId());

        assertEqualExercise(exercise, exerciseFound);
    }

    @Test
    void findMany_ValidExerciseIds_ReturnSetOfExerciseEntity() {
        Set<ExerciseEntity> exercisesFound = exerciseServiceImpl.findMany(Set.of(exercise.getId(), exerciseBis.getId()));

        assertEqualExerciseList(List.of(exercise, exerciseBis), exercisesFound.stream().toList());
    }

    @Test
    void save_AddExercise_ReturnExerciseEntity() {
        ExerciseEntity unsavedExercise = createTestExercise(null);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(unsavedExercise);

        assertEqualExercise(unsavedExercise, exerciseSaved);
    }

    @Test
    void update_UpdateExerciseWithInvalidId_ThrowExerciseNotFoundException() {
        ExerciseEntity exercise = this.exercise;
        exercise.setId(exercise.getId());
        exerciseRepository.delete(exercise);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.save(exercise));
    }

    @Test
    void update_ValidInputExercise_ReturnExerciseEntity() {
        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(exercise);

        assertEqualExercise(exercise, exerciseSaved);
    }

    @Test
    void delete_InvalidExerciseId_ThrowExerciseNotFoundException() {
        exerciseRepository.delete(exercise);

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void updateExerciseTypeRelation_ValidInputs_Void() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        Set<Long> oldExerciseIds = Set.of(exercise.getId());
        Set<Long> newExerciseIds = Set.of(exerciseBis.getId());
        exercise.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exercise);

        exerciseServiceImpl.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType);

        Optional<ExerciseEntity> exerciseOneReturn = exerciseRepository.findById(exercise.getId());
        Optional<ExerciseEntity> exerciseTwoReturn = exerciseRepository.findById(exerciseBis.getId());
        assertTrue(exerciseOneReturn.isPresent());
        assertTrue(exerciseTwoReturn.isPresent());
        assertEquals(0, exerciseOneReturn.get().getExerciseTypes().size());
        assertEquals(1, exerciseTwoReturn.get().getExerciseTypes().size());
        assertEquals(exerciseType.getId(), exerciseTwoReturn.get().getExerciseTypes().stream().toList().getFirst().getId());
    }

    @Test
    void updateMuscleRelation_ValidInputs_Void() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exercise.getId());
        newExerciseIds.add(exerciseBis.getId());
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);

        exerciseServiceImpl.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle);

        Optional<ExerciseEntity> exerciseOneReturn = exerciseRepository.findById(exercise.getId());
        Optional<ExerciseEntity> exerciseTwoReturn = exerciseRepository.findById(exerciseBis.getId());
        assertTrue(exerciseOneReturn.isPresent());
        assertTrue(exerciseTwoReturn.isPresent());
        assertEquals(0, exerciseOneReturn.get().getMuscles().size());
        assertEquals(1, exerciseTwoReturn.get().getMuscles().size());
        assertEquals(muscle.getId(), exerciseTwoReturn.get().getMuscles().stream().toList().getFirst().getId());
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean ExerciseFound = exerciseServiceImpl.exists(exercise.getId());

        Assertions.assertTrue(ExerciseFound);
    }

    private void assertEqualExerciseList(
            List<ExerciseEntity> expectedExerciseList,
            List<ExerciseEntity> exerciseListObtained
    ) {
        Assertions.assertEquals(expectedExerciseList.size(), exerciseListObtained.size());
        expectedExerciseList.forEach(exerciseFound -> assertEqualExercise(
                exerciseListObtained.stream().filter(
                        exercise -> Objects.equals(exercise.getId(), exerciseFound.getId())
                ).toList().getFirst(),
                exerciseFound)
        );
    }
}
