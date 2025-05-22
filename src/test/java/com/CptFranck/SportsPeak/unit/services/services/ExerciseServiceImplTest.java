package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.ExerciseServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExerciseList;
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
    void findAll_ValidUse_ReturnListOfExerciseEntity() {
        List<ExerciseEntity> exerciseList = createTestExerciseList(false);
        when(exerciseRepository.findAll()).thenReturn(exerciseList);

        List<ExerciseEntity> exerciseFound = exerciseServiceImpl.findAll();

        Assertions.assertEquals(exerciseList, exerciseFound);
    }

    @Test
    void findOne_InvalidExerciseId_ThrowExerciseNotFoundException() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.findOne(exercise.getId() + 1));
    }

    @Test
    void findOne_ValidExerciseId_ReturnExerciseEntity() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));

        ExerciseEntity exerciseFound = exerciseServiceImpl.findOne(exercise.getId());

        Assertions.assertEquals(exercise, exerciseFound);
    }

    @Test
    void findMany_ValidExerciseIds_ReturnSetOfExerciseEntity() {
        List<ExerciseEntity> exerciseList = createTestExerciseList(false);
        Set<Long> ExerciseIds = exerciseList.stream().map(ExerciseEntity::getId).collect(Collectors.toSet());
        when(exerciseRepository.findAllById(Mockito.anyIterable())).thenReturn(exerciseList);

        Set<ExerciseEntity> exerciseFound = exerciseServiceImpl.findMany(ExerciseIds);

        Assertions.assertEquals(new HashSet<>(exerciseList), exerciseFound);
    }

    @Test
    void save_AddExercise_ReturnExerciseEntity() {
        ExerciseEntity unsavedExercise = createTestExercise(null);
        when(exerciseRepository.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(unsavedExercise);

        Assertions.assertEquals(exerciseSaved, exercise);
    }

    @Test
    void save_UpdateExerciseNotExisting_ThrowExerciseNotFoundException() {
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        Assertions.assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.save(exercise));
    }

    @Test
    void save_UpdateExercise_ReturnExerciseEntity() {
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(exerciseRepository.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);

        ExerciseEntity exerciseSaved = exerciseServiceImpl.save(exercise);

        Assertions.assertEquals(exerciseSaved, exercise);
    }

    @Test
    void updateExerciseTypeRelation_ValidInputs_Void() {
        ExerciseEntity exerciseBis = createTestExercise(2L);
        Set<Long> oldExerciseIds = Set.of(exercise.getId());
        Set<Long> newExerciseIds = Set.of(exerciseBis.getId());
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        exercise.getExerciseTypes().add(exerciseType);
        when(exerciseRepository.findAllById(oldExerciseIds)).thenReturn(List.of(exercise));
        when(exerciseRepository.findAllById(newExerciseIds)).thenReturn(List.of(exerciseBis));
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        assertAll(() -> exerciseServiceImpl.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType));
        Assertions.assertEquals(0, exercise.getExerciseTypes().size());
        Assertions.assertEquals(1, exerciseBis.getExerciseTypes().size());
    }

    @Test
    void updateMuscleRelation_ValidInputs_Void() {
        ExerciseEntity exerciseBis = createTestExercise(2L);
        Set<Long> oldExerciseIds = Set.of(exercise.getId());
        Set<Long> newExerciseIds = Set.of(exerciseBis.getId());
        MuscleEntity muscle = createTestMuscle(1L);
        exercise.getMuscles().add(muscle);

        when(exerciseRepository.findAllById(oldExerciseIds)).thenReturn(List.of(exercise));
        when(exerciseRepository.findAllById(newExerciseIds)).thenReturn(List.of(exerciseBis));
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        assertAll(() -> exerciseServiceImpl.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle));
        Assertions.assertEquals(0, exercise.getMuscles().size());
        Assertions.assertEquals(1, exerciseBis.getMuscles().size());
    }

    @Test
    void delete_InvalidExerciseId_ThrowExerciseNotFoundException() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));

        assertAll(() -> exerciseServiceImpl.delete(exercise.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean ExerciseFound = exerciseServiceImpl.exists(exercise.getId());

        Assertions.assertTrue(ExerciseFound);
    }
}
