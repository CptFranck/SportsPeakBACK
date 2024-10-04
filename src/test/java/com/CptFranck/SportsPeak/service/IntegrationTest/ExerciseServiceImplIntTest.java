package com.CptFranck.SportsPeak.service.IntegrationTest;

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
import org.springframework.transaction.annotation.Transactional;

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
        this.muscleRepository.deleteAll();
        this.exerciseTypeRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.userRepository.deleteAll();
        this.exerciseRepository.deleteAll();
    }

    @Test
    void exerciseService_Save_Success() {
        ExerciseEntity unsavedExercise = createTestExercise(null);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(unsavedExercise);

        Assertions.assertEquals(unsavedExercise, exerciseSaved);
    }

    @Test
    void exerciseService_FindAll_Success() {
        List<ExerciseEntity> exerciseList = createTestExerciseList(true);
        exerciseList = StreamSupport.stream(
                exerciseRepository.saveAll(exerciseList).spliterator(),
                false).toList();

        List<ExerciseEntity> exercisesFound = exerciseServiceImpl.findAll();

        assertEqualExerciseList(exerciseList, exercisesFound.stream().toList());
    }

    @Test
    void exerciseService_FindOne_Success() {
        ExerciseEntity exerciseSaved = createTestExercise(null);
        exerciseSaved = exerciseRepository.save(exerciseSaved);

        Optional<ExerciseEntity> exerciseFound = exerciseServiceImpl.findOne(exerciseSaved.getId());

        Assertions.assertTrue(exerciseFound.isPresent());
        assertEqualExercise(exerciseSaved, exerciseFound.get());
    }

    @Test
    void exerciseService_FindMany_Success() {
        List<ExerciseEntity> exerciseList = createTestExerciseList(true);
        exerciseList = StreamSupport.stream(
                exerciseRepository.saveAll(exerciseList).spliterator(),
                false).toList();
        Set<Long> ExerciseIds = exerciseList.stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        Set<ExerciseEntity> exercisesFound = exerciseServiceImpl.findMany(ExerciseIds);

        assertEqualExerciseList(exerciseList, exercisesFound.stream().toList());
    }

    @Test
    @Transactional
    void exerciseService_UpdateExerciseTypeRelation_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        ExerciseEntity exerciseWithTypeToRemove = exerciseRepository.save(createTestExercise(null));
        ExerciseEntity exerciseWithTypeToAdd = exerciseRepository.save(createTestExercise(null));
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exerciseWithTypeToRemove.getId());
        newExerciseIds.add(exerciseWithTypeToAdd.getId());
        exerciseWithTypeToRemove.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exerciseWithTypeToRemove);

        exerciseServiceImpl.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType);

        assertEquals(0, exerciseWithTypeToRemove.getExerciseTypes().size());
        assertEquals(1, exerciseWithTypeToAdd.getExerciseTypes().size());
        assertEquals(exerciseType.getId(), exerciseWithTypeToAdd.getExerciseTypes().stream().toList().getFirst().getId());
    }

    @Test
    @Transactional
    void exerciseService_UpdateMuscleRelation_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        ExerciseEntity exerciseWithTypeToRemove = exerciseRepository.save(createTestExercise(null));
        ExerciseEntity exerciseWithTypeToAdd = exerciseRepository.save(createTestExercise(null));
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exerciseWithTypeToRemove.getId());
        newExerciseIds.add(exerciseWithTypeToAdd.getId());
        exerciseWithTypeToRemove.getMuscles().add(muscle);
        exerciseRepository.save(exerciseWithTypeToRemove);

        exerciseServiceImpl.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle);

        assertEquals(0, exerciseWithTypeToRemove.getMuscles().size());
        assertEquals(1, exerciseWithTypeToAdd.getMuscles().size());
        assertEquals(muscle.getId(), exerciseWithTypeToAdd.getMuscles().stream().toList().getFirst().getId());
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
