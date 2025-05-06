package com.CptFranck.SportsPeak.unit.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.impl.ExerciseTypeServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseTypeList;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseTypeServiceImplTest {

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private ExerciseTypeRepository exerciseTypeRepository;

    @InjectMocks
    private ExerciseTypeServiceImpl exerciseTypeServiceImpl;

    private ExerciseTypeEntity exerciseType;

    @BeforeEach
    void setUp() {
        exerciseType = createTestExerciseType(1L);
    }

    @Test
    void findAll_ValidUse_ReturnListOfExerciseTypeEntity() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList(false);
        when(exerciseTypeRepository.findAll()).thenReturn(exerciseTypeList);

        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeServiceImpl.findAll();

        Assertions.assertEquals(exerciseTypeList, exerciseTypeFound);
    }

    @Test
    void findOne_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.findOne(exerciseType.getId() + 1));
    }

    @Test
    void findOne_ValidExerciseTypeId_ReturnExerciseTypeEntity() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        ExerciseTypeEntity exerciseTypeFound = exerciseTypeServiceImpl.findOne(exerciseType.getId());

        Assertions.assertEquals(exerciseType, exerciseTypeFound);
    }

    @Test
    void findMany_ValidExerciseTypeIds_ReturnSetOfExerciseTypeEntity() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList(false);
        Set<Long> exerciseTypeIds = exerciseTypeList.stream().map(ExerciseTypeEntity::getId).collect(Collectors.toSet());
        when(exerciseTypeRepository.findAllById(Mockito.anyIterable())).thenReturn(exerciseTypeList);

        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeServiceImpl.findMany(exerciseTypeIds);
        Assertions.assertEquals(new HashSet<>(exerciseTypeList), exerciseTypeFound);
    }

    @Test
    void save_AddNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(null);
        when(exerciseTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeServiceImpl.save(unsavedExerciseType);

        Assertions.assertEquals(exerciseTypeSaved, exerciseType);
    }

    @Test
    void save_UpdateExerciseTypeWithInvalidId_ThrowExerciseTypeNotFoundException() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId() + 1);
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.save(unsavedExerciseType));
    }

    @Test
    void save_UpdateExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId());
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));
        when(exerciseTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeServiceImpl.save(unsavedExerciseType);

        Assertions.assertEquals(exerciseTypeSaved, exerciseType);
    }

    @Test
    void delete_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void delete_ExerciseTypeStillUsed_ThrowMuscleNotFoundException() {
        exerciseType.getExercises().add(createTestExercise(1L));
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        assertThrows(ExerciseTypeStillUsedInExerciseException.class, () -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        assertAll(() -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(exerciseTypeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean exerciseTypeFound = exerciseTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }
}
