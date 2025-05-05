package com.CptFranck.SportsPeak.unit.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseTypeServiceImplTest {

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
    void create_ValidInputNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(null);
        when(exerciseTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeServiceImpl.create(unsavedExerciseType);

        Assertions.assertEquals(exerciseTypeSaved, exerciseType);
    }

    @Test
    void update_ExerciseTypeNotFound_ThrowExerciseTypeNotFoundException() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId() + 1);
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.update(unsavedExerciseType));
    }

    @Test
    void update_ValidInputExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId() + 1);
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));
        when(exerciseTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeServiceImpl.update(unsavedExerciseType);

        Assertions.assertEquals(exerciseTypeSaved, exerciseType);
    }

    @Test
    void findAll_Valid_ReturnListOfExerciseTypeEntity() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList(false);
        when(exerciseTypeRepository.findAll()).thenReturn(exerciseTypeList);

        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeServiceImpl.findAll();

        Assertions.assertEquals(exerciseTypeList, exerciseTypeFound);
    }

    @Test
    void findOne_ExerciseTypeNotFound_ThrowExerciseTypeNotFoundException() {
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
    void exists_ValidInput_ReturnTrue() {
        when(exerciseTypeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean exerciseTypeFound = exerciseTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }

    @Test
    void delete_ExerciseTypeNotFound_ThrowExerciseTypeNotFoundException() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        assertAll(() -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }
}
