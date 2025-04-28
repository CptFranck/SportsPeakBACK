package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExerciseList;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseServiceImplIntTest {

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
    void exerciseService_Save_Success() {
        ExerciseEntity unsavedExercise = createTestExercise(null);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(unsavedExercise);

        assertEqualExercise(unsavedExercise, exerciseSaved);
    }

    @Test
    void exerciseService_FindAll_Success() {
        List<ExerciseEntity> exerciseList = StreamSupport.stream(
                exerciseRepository.saveAll(createTestExerciseList(true)).spliterator(),
                false).toList();

        List<ExerciseEntity> exercisesFound = exerciseServiceImpl.findAll();

        assertEqualExerciseList(exerciseList, exercisesFound.stream().toList());
    }

    @Test
    void exerciseService_FindOne_Success() {
        Optional<ExerciseEntity> exerciseFound = exerciseServiceImpl.findOne(exercise.getId());

        Assertions.assertTrue(exerciseFound.isPresent());
        assertEqualExercise(exercise, exerciseFound.get());
    }

    @Test
    void exerciseService_FindMany_Success() {
        List<ExerciseEntity> exerciseList = StreamSupport.stream(
                exerciseRepository.saveAll(createTestExerciseList(true)).spliterator(),
                false).toList();
        Set<Long> ExerciseIds = exerciseList.stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        Set<ExerciseEntity> exercisesFound = exerciseServiceImpl.findMany(ExerciseIds);

        assertEqualExerciseList(exerciseList, exercisesFound.stream().toList());
    }

    @Test
    void exerciseService_UpdateExerciseTypeRelation_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exercise.getId());
        newExerciseIds.add(exerciseBis.getId());
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
    void exerciseService_UpdateMuscleRelation_Success() {
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
    void exerciseService_UpdateProgExerciseRelation_Success() {
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
    void exerciseService_Exists_Success() {
        boolean ExerciseFound = exerciseServiceImpl.exists(exercise.getId());

        Assertions.assertTrue(ExerciseFound);
    }

    @Test
    void exerciseService_Delete_Success() {
        assertAll(() -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void exerciseService_Delete_Unsuccessful() {
        exerciseRepository.delete(exercise);

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(exercise.getId()));
    }

    private void assertEqualExerciseList(
            List<ExerciseEntity> expectedExerciseList,
            List<ExerciseEntity> exerciseListObtained
    ) {
        expectedExerciseList.forEach(exerciseFound -> assertEqualExercise(
                exerciseListObtained.stream().filter(
                        exercise -> Objects.equals(exercise.getId(), exerciseFound.getId())
                ).toList().getFirst(),
                exerciseFound)
        );
    }

    private void assertEqualExercise(ExerciseEntity expected, ExerciseEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getGoal(), actual.getGoal());
    }
}
