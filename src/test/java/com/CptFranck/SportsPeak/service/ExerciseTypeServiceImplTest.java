package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.impl.ExerciseTypeServiceImpl;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseTypeList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseTypeServiceImplTest {

    @Mock
    ExerciseTypeRepository exerciseTypeRepository;

    @InjectMocks
    ExerciseTypeServiceImpl exerciseTypeTypeServiceImpl;

    @Test
    void exerciseTypeService_Save_Success() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(null);
        ExerciseTypeEntity exerciseTypeSavedInRepository = createTestExerciseType(1L);
        when(exerciseTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeSavedInRepository);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeTypeServiceImpl.save(exerciseType);

        Assertions.assertEquals(exerciseTypeSavedInRepository, exerciseTypeSaved);
    }

    @Test
    void exerciseTypeService_FindAll_Success() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList();
        when(exerciseTypeRepository.findAll()).thenReturn(exerciseTypeList);

        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findAll();

        Assertions.assertEquals(exerciseTypeList, exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_FindOne_Success() {
        ExerciseTypeEntity exerciseTypeEntity = createTestExerciseType(1L);
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseTypeEntity));

        Optional<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findOne(exerciseTypeEntity.getId());

        Assertions.assertTrue(exerciseTypeFound.isPresent());
        Assertions.assertEquals(exerciseTypeEntity, exerciseTypeFound.get());
    }

    @Test
    void exerciseTypeService_FindMany_Success() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList();
        Set<Long> exerciseTypeIds = exerciseTypeList.stream().map(ExerciseTypeEntity::getId).collect(Collectors.toSet());
        when(exerciseTypeRepository.findAllById(Mockito.anyIterable())).thenReturn(exerciseTypeList);

        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findMany(exerciseTypeIds);
        Assertions.assertEquals(new HashSet<>(exerciseTypeList), exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Exists_Success() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        when(exerciseTypeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean exerciseTypeFound = exerciseTypeTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Delete_Success() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        assertAll(() -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exerciseTypeService_Delete_Unsuccessful() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }
}
