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
    ExerciseTypeRepository ExerciseTypeTypeRepository;

    @InjectMocks
    ExerciseTypeServiceImpl ExerciseTypeTypeServiceImpl;

    @Test
    void exerciseTypeService_Save_Success() {
        ExerciseTypeEntity ExerciseTypeEntity = createTestExerciseType(null);
        ExerciseTypeEntity ExerciseTypeSavedInRepository = createTestExerciseType(1L);
        when(ExerciseTypeTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(ExerciseTypeSavedInRepository);

        ExerciseTypeEntity ExerciseTypeSaved = ExerciseTypeTypeServiceImpl.save(ExerciseTypeEntity);

        Assertions.assertEquals(ExerciseTypeSavedInRepository, ExerciseTypeSaved);
    }

    @Test
    void exerciseTypeService_FindAll_Success() {
        List<ExerciseTypeEntity> ExerciseTypeList = createTestExerciseTypeList();
        when(ExerciseTypeTypeRepository.findAll()).thenReturn(ExerciseTypeList);

        List<ExerciseTypeEntity> ExerciseTypeFound = ExerciseTypeTypeServiceImpl.findAll();

        Assertions.assertEquals(ExerciseTypeList, ExerciseTypeFound);
    }

    @Test
    void exerciseTypeService_FindOne_Success() {
        ExerciseTypeEntity ExerciseTypeEntity = createTestExerciseType(1L);
        when(ExerciseTypeTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(ExerciseTypeEntity));

        Optional<ExerciseTypeEntity> ExerciseTypeFound = ExerciseTypeTypeServiceImpl.findOne(ExerciseTypeEntity.getId());

        Assertions.assertTrue(ExerciseTypeFound.isPresent());
        Assertions.assertEquals(ExerciseTypeEntity, ExerciseTypeFound.get());
    }

    @Test
    void exerciseTypeService_FindMany_Success() {
        List<ExerciseTypeEntity> ExerciseTypeList = createTestExerciseTypeList();
        Set<Long> ExerciseTypeIds = ExerciseTypeList.stream().map(ExerciseTypeEntity::getId).collect(Collectors.toSet());
        when(ExerciseTypeTypeRepository.findAllById(Mockito.anyIterable())).thenReturn(ExerciseTypeList);

        Set<ExerciseTypeEntity> ExerciseTypeFound = ExerciseTypeTypeServiceImpl.findMany(ExerciseTypeIds);
        Assertions.assertEquals(new HashSet<>(ExerciseTypeList), ExerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Exists_Success() {
        ExerciseTypeEntity ExerciseTypeEntity = createTestExerciseType(1L);
        when(ExerciseTypeTypeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean ExerciseTypeFound = ExerciseTypeTypeServiceImpl.exists(ExerciseTypeEntity.getId());

        Assertions.assertTrue(ExerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Delete_Success() {
        ExerciseTypeEntity ExerciseTypeEntity = createTestExerciseType(1L);
        when(ExerciseTypeTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(ExerciseTypeEntity));

        assertAll(() -> ExerciseTypeTypeServiceImpl.delete(ExerciseTypeEntity.getId()));
    }

    @Test
    void exerciseTypeService_Delete_Unsuccessful() {
        ExerciseTypeEntity ExerciseTypeEntity = createTestExerciseType(1L);
        when(ExerciseTypeTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseTypeNotFoundException.class, () -> ExerciseTypeTypeServiceImpl.delete(ExerciseTypeEntity.getId()));
    }
}
