package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.Assertions;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    @Mock
    ExerciseRepository exerciseRepository;

    @InjectMocks
    ExerciseServiceImpl exerciseServiceImpl;

    @Test
    void exerciseService_Save_Success() {
        ExerciseEntity ExerciseEntity = createTestExercise(null);
        ExerciseEntity ExerciseSavedInRepository = createTestExercise(1L);
        when(exerciseRepository.save(Mockito.any(ExerciseEntity.class))).thenReturn(ExerciseSavedInRepository);

        ExerciseEntity ExerciseSaved = exerciseServiceImpl.save(ExerciseEntity);

        Assertions.assertEquals(ExerciseSavedInRepository, ExerciseSaved);
    }

    @Test
    void exerciseService_FindAll_Success() {
        List<ExerciseEntity> ExerciseList = createTestExerciseList();
        when(exerciseRepository.findAll()).thenReturn(ExerciseList);

        List<ExerciseEntity> ExerciseFound = exerciseServiceImpl.findAll();

        Assertions.assertEquals(ExerciseList, ExerciseFound);
    }

    @Test
    void exerciseService_FindOne_Success() {
        ExerciseEntity ExerciseEntity = createTestExercise(1L);
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(ExerciseEntity));

        Optional<ExerciseEntity> ExerciseFound = exerciseServiceImpl.findOne(ExerciseEntity.getId());

        Assertions.assertTrue(ExerciseFound.isPresent());
        Assertions.assertEquals(ExerciseEntity, ExerciseFound.get());
    }

    @Test
    void exerciseService_FindMany_Success() {
        List<ExerciseEntity> ExerciseList = createTestExerciseList();
        Set<Long> ExerciseIds = ExerciseList.stream().map(ExerciseEntity::getId).collect(Collectors.toSet());
        when(exerciseRepository.findAllById(Mockito.anyIterable())).thenReturn(ExerciseList);

        Set<ExerciseEntity> ExerciseFound = exerciseServiceImpl.findMany(ExerciseIds);
        Assertions.assertEquals(new HashSet<>(ExerciseList), ExerciseFound);
    }

    @Test
    void exerciseService_Exists_Success() {
        ExerciseEntity ExerciseEntity = createTestExercise(1L);
        when(exerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean ExerciseFound = exerciseServiceImpl.exists(ExerciseEntity.getId());

        Assertions.assertTrue(ExerciseFound);
    }

    @Test
    void exerciseService_Delete_Success() {
        ExerciseEntity ExerciseEntity = createTestExercise(1L);
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(ExerciseEntity));

        assertAll(() -> exerciseServiceImpl.delete(ExerciseEntity.getId()));
    }

    @Test
    void exerciseService_Delete_Unsuccessful() {
        ExerciseEntity ExerciseEntity = createTestExercise(1L);
        when(exerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> exerciseServiceImpl.delete(ExerciseEntity.getId()));
    }
}
