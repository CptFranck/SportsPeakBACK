package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.repositories.*;
import com.CptFranck.SportsPeak.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.*;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
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
    void create_ValidInputNewExercise_ReturnExerciseEntity() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();

        ExerciseEntity exerciseSaved = exerciseServiceImpl.create(inputNewExercise);

        assertEqualInputNewExercise(inputNewExercise, exerciseSaved);
    }

    @Test
    void update_ExerciseNotFound_ThrowExerciseNotFoundException() {
        InputExercise inputExercise = createTestInputExercise(exercise.getId() + exerciseBis.getId() + 1);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.update(inputExercise));
    }

    @Test
    void update_ValidInputExercise_ReturnExerciseEntity() {
        InputExercise inputExercise = createTestInputExercise(exercise.getId() + 1);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.update(inputExercise);

        assertEqualInputNewExercise(inputExercise, exerciseSaved);
    }

    @Test
    void save_ValidExerciseEntity_ReturnExerciseEntity() {
        ExerciseEntity unsavedExercise = createTestExercise(null);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(unsavedExercise);

        assertEqualExercise(unsavedExercise, exerciseSaved);
    }

    @Test
    void findAll_Valid_ReturnListOfExerciseEntity() {
        List<ExerciseEntity> exercisesFound = exerciseServiceImpl.findAll();

        assertEqualExerciseList(List.of(exercise, exerciseBis), exercisesFound.stream().toList());
    }

    @Test
    void findOne_ExerciseNotFound_ThrowExerciseNotFoundException() {
        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.findOne(exercise.getId() + exerciseBis.getId() + 1));
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
    void updateProgExerciseRelation_ValidInputs_Void() {
        UserEntity user = userRepository.save(createTestUser(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        exercise.getProgExercises().add(progExercise);
        exerciseRepository.save(exercise);

        exerciseServiceImpl.updateProgExerciseRelation(exerciseBis, exercise, progExercise);

        assertEquals(0, exercise.getProgExercises().size());
        assertEquals(1, exerciseBis.getProgExercises().size());
        assertEquals(progExercise.getId(), exerciseBis.getProgExercises().stream().toList().getFirst().getId());
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean ExerciseFound = exerciseServiceImpl.exists(exercise.getId());

        Assertions.assertTrue(ExerciseFound);
    }

    @Test
    void delete_ExerciseNotFound_ThrowExerciseNotFoundException() {
        exerciseRepository.delete(exercise);

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> exerciseServiceImpl.delete(exercise.getId()));
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

    private void assertEqualInputNewExercise(InputNewExercise expected, ExerciseEntity actual) {
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getGoal(), actual.getGoal());
        Assertions.assertEquals(expected.getMuscleIds().size(), actual.getMuscles().size());
        Assertions.assertEquals(expected.getExerciseTypeIds().size(), actual.getExerciseTypes().size());
    }

    private void assertEqualExercise(ExerciseEntity expected, ExerciseEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getGoal(), actual.getGoal());
        Assertions.assertEquals(expected.getMuscles().size(), actual.getMuscles().size());
        Assertions.assertEquals(expected.getExerciseTypes().size(), actual.getExerciseTypes().size());
        Assertions.assertEquals(expected.getProgExercises().size(), actual.getProgExercises().size());
    }
}
