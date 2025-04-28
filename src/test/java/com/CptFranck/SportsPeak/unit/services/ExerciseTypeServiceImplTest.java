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
    private ExerciseTypeServiceImpl exerciseTypeTypeServiceImpl;

    private ExerciseTypeEntity exerciseType;

    @BeforeEach
    void setUp() {
        exerciseType = createTestExerciseType(1L);
    }

    @Test
    void exerciseTypeService_Save_Success() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(null);
        when(exerciseTypeRepository.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeTypeServiceImpl.save(unsavedExerciseType);

        Assertions.assertEquals(exerciseType, exerciseTypeSaved);
    }

    @Test
    void exerciseTypeService_FindAll_Success() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList(false);
        when(exerciseTypeRepository.findAll()).thenReturn(exerciseTypeList);

        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findAll();

        Assertions.assertEquals(exerciseTypeList, exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_FindOne_Success() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        Optional<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findOne(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound.isPresent());
        Assertions.assertEquals(exerciseType, exerciseTypeFound.get());
    }

    @Test
    void exerciseTypeService_FindMany_Success() {
        List<ExerciseTypeEntity> exerciseTypeList = createTestExerciseTypeList(false);
        Set<Long> exerciseTypeIds = exerciseTypeList.stream().map(ExerciseTypeEntity::getId).collect(Collectors.toSet());
        when(exerciseTypeRepository.findAllById(Mockito.anyIterable())).thenReturn(exerciseTypeList);

        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findMany(exerciseTypeIds);
        Assertions.assertEquals(new HashSet<>(exerciseTypeList), exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Exists_Success() {
        when(exerciseTypeRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean exerciseTypeFound = exerciseTypeTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Delete_Success() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));

        assertAll(() -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exerciseTypeService_Delete_Unsuccessful() {
        when(exerciseTypeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }
}
