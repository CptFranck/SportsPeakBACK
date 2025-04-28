package com.CptFranck.SportsPeak.unit.service;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseList;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseServiceImpl;

    private ExerciseEntity exercise;

    @BeforeEach
    void setUp() {
        exercise = createTestExercise(1L);
    }

    @Test
    void exerciseService_Save_Success() {
        ExerciseEntity unsavedExercise = createTestExercise(null);
        when(exerciseRepository.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(unsavedExercise);

        Assertions.assertEquals(exerciseSaved, exercise);
    }

    @Test
    void exerciseService_FindAll_Success() {
        List<ExerciseEntity> exerciseList = createTestExerciseList(false);
        when(exerciseRepository.findAll()).thenReturn(exerciseList);

        List<ExerciseEntity> exerciseFound = exerciseServiceImpl.findAll();

        Assertions.assertEquals(exerciseList, exerciseFound);
    }

    @Test
    void exerciseService_FindOne_Success() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));

        Optional<ExerciseEntity> exerciseFound = exerciseServiceImpl.findOne(exercise.getId());

        Assertions.assertTrue(exerciseFound.isPresent());
        Assertions.assertEquals(exercise, exerciseFound.get());
    }

    @Test
    void exerciseService_FindMany_Success() {
        List<ExerciseEntity> exerciseList = createTestExerciseList(false);
        Set<Long> ExerciseIds = exerciseList.stream().map(ExerciseEntity::getId).collect(Collectors.toSet());
        when(exerciseRepository.findAllById(Mockito.anyIterable())).thenReturn(exerciseList);

        Set<ExerciseEntity> exerciseFound = exerciseServiceImpl.findMany(ExerciseIds);

        Assertions.assertEquals(new HashSet<>(exerciseList), exerciseFound);
    }

    @Test
    void exerciseService_UpdateExerciseTypeRelation_Success() {
        ExerciseEntity exerciseBis = createTestExercise(2L);
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exercise.getId());
        newExerciseIds.add(exerciseBis.getId());
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);

        when(exerciseRepository.findAllById(oldExerciseIds)).thenReturn(List.of(exercise));
        when(exerciseRepository.findAllById(newExerciseIds)).thenReturn(List.of(exerciseBis));

        assertAll(() -> exerciseServiceImpl.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType));
    }

    @Test
    void exerciseService_UpdateMuscleRelation_Success() {
        ExerciseEntity exerciseBis = createTestExercise(2L);
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = new HashSet<>();
        oldExerciseIds.add(exercise.getId());
        newExerciseIds.add(exerciseBis.getId());
        MuscleEntity muscle = createTestMuscle(1L);

        when(exerciseRepository.findAllById(oldExerciseIds)).thenReturn(List.of(exercise));
        when(exerciseRepository.findAllById(newExerciseIds)).thenReturn(List.of(exerciseBis));

        assertAll(() -> exerciseServiceImpl.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle));
    }

    @Test
    void exerciseService_UpdateProgExerciseRelation_Success() {
        UserEntity user = createTestUser(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        exercise.getProgExercises().add(progExercise);
        ExerciseEntity newExercise = createTestExercise(2L);

        when(exerciseRepository.save(newExercise)).thenReturn(newExercise);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        assertAll(() -> exerciseServiceImpl.updateProgExerciseRelation(newExercise, exercise, progExercise));
    }

    @Test
    void exerciseService_Exists_Success() {
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean ExerciseFound = exerciseServiceImpl.exists(exercise.getId());

        Assertions.assertTrue(ExerciseFound);
    }

    @Test
    void exerciseService_Delete_Success() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));

        assertAll(() -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void exerciseService_Delete_Unsuccessful() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(exercise.getId()));
    }
}
