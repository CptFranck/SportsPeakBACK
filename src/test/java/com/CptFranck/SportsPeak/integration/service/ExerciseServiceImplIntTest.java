package com.CptFranck.SportsPeak.integration.service;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.*;
import com.CptFranck.SportsPeak.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseList;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
        ExerciseEntity exerciseSaved = exerciseRepository.save(createTestExercise(null));

        Optional<ExerciseEntity> exerciseFound = exerciseServiceImpl.findOne(exerciseSaved.getId());

        Assertions.assertTrue(exerciseFound.isPresent());
        assertEqualExercise(exerciseSaved, exerciseFound.get());
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
        ExerciseEntity exerciseOne = exerciseRepository.save(createTestExercise(null));
        ExerciseEntity exerciseTwo = exerciseRepository.save(createTestExercise(null));
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exerciseOne.getId());
        newExerciseIds.add(exerciseTwo.getId());
        exerciseOne.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exerciseOne);

        exerciseServiceImpl.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType);


        Optional<ExerciseEntity> exerciseOneReturn = exerciseRepository.findById(exerciseOne.getId());
        Optional<ExerciseEntity> exerciseTwoReturn = exerciseRepository.findById(exerciseTwo.getId());
        assertTrue(exerciseOneReturn.isPresent());
        assertTrue(exerciseTwoReturn.isPresent());
        assertEquals(0, exerciseOneReturn.get().getExerciseTypes().size());
        assertEquals(1, exerciseTwoReturn.get().getExerciseTypes().size());
        assertEquals(exerciseType.getId(), exerciseTwoReturn.get().getExerciseTypes().stream().toList().getFirst().getId());
    }

    @Test
    void exerciseService_UpdateMuscleRelation_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        ExerciseEntity exerciseOne = exerciseRepository.save(createTestExercise(null));
        ExerciseEntity exerciseTwo = exerciseRepository.save(createTestExercise(null));
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exerciseOne.getId());
        newExerciseIds.add(exerciseTwo.getId());
        exerciseOne.getMuscles().add(muscle);
        exerciseRepository.save(exerciseOne);

        exerciseServiceImpl.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle);

        Optional<ExerciseEntity> exerciseOneReturn = exerciseRepository.findById(exerciseOne.getId());
        Optional<ExerciseEntity> exerciseTwoReturn = exerciseRepository.findById(exerciseTwo.getId());
        assertTrue(exerciseOneReturn.isPresent());
        assertTrue(exerciseTwoReturn.isPresent());
        assertEquals(0, exerciseOneReturn.get().getMuscles().size());
        assertEquals(1, exerciseTwoReturn.get().getMuscles().size());
        assertEquals(muscle.getId(), exerciseTwoReturn.get().getMuscles().stream().toList().getFirst().getId());
    }

    @Test
    void exerciseService_UpdateProgExerciseRelation_Success() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity oldExercise = exerciseRepository.save(createTestExercise(null));
        ExerciseEntity newExercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(1L, user, oldExercise));
        oldExercise.getProgExercises().add(progExercise);
        exerciseRepository.save(oldExercise);

        exerciseServiceImpl.updateProgExerciseRelation(newExercise, oldExercise, progExercise);

        assertEquals(0, oldExercise.getProgExercises().size());
        assertEquals(1, newExercise.getProgExercises().size());
        assertEquals(progExercise.getId(), newExercise.getProgExercises().stream().toList().getFirst().getId());
    }

    @Test
    void exerciseService_Exists_Success() {
        ExerciseEntity exerciseSaved = exerciseRepository.save(createTestExercise(null));

        boolean ExerciseFound = exerciseServiceImpl.exists(exerciseSaved.getId());

        Assertions.assertTrue(ExerciseFound);
    }

    @Test
    void exerciseService_Delete_Success() {
        ExerciseEntity exerciseSaved = exerciseRepository.save(createTestExercise(null));

        assertAll(() -> exerciseServiceImpl.delete(exerciseSaved.getId()));
    }

    @Test
    void exerciseService_Delete_Unsuccessful() {
        ExerciseEntity exerciseSaved = exerciseRepository.save(createTestExercise(null));
        exerciseRepository.delete(exerciseSaved);

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(exerciseSaved.getId()));
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
