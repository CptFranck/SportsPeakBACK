package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.ExerciseTypeController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseTypeControllerTest {

    @InjectMocks
    private ExerciseTypeController exerciseTypeController;

    @Mock
    private Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private ExerciseTypeService exerciseTypeService;

    private ExerciseTypeEntity exerciseType;
    private ExerciseTypeDto exerciseTypeDto;

    @BeforeEach
    void init() {
        exerciseType = createTestExerciseType(1L);
        exerciseTypeDto = createTestExerciseTypeDto(1L);
    }

    @Test
    void ExerciseTypeController_GetExerciseTypes_Success() {
        when(exerciseTypeService.findAll()).thenReturn(List.of(exerciseType));
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        List<ExerciseTypeDto> exerciseTypeDtos = exerciseTypeController.getExerciseTypes();

        Assertions.assertNotNull(exerciseTypeDtos);
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Unsuccessful() {
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.getExerciseTypeById(1L);

        Assertions.assertNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Success() {
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.getExerciseTypeById(1L);

        Assertions.assertNotNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_AddExerciseType_Success() {
        Set<ExerciseEntity> exercises = new HashSet<>();
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(exerciseTypeService.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(this.exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(createTestInputNewExerciseType());

        Assertions.assertNotNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_ModifyExerciseType_Unsuccessful() {
        when(exerciseTypeService.exists(Mockito.any(Long.class))).thenReturn(false);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.modifyExerciseType(createTestInputExerciseType());

        Assertions.assertNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_ModifyExerciseType_Success() {

        Set<ExerciseEntity> exercises = new HashSet<>();
        when(exerciseTypeService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));
        when(exerciseTypeService.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.modifyExerciseType(createTestInputExerciseType());

        Assertions.assertNotNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_DeleteExercise_Success() {
        Long id = exerciseTypeController.deleteExerciseType(1L);

        Assertions.assertNotNull(id);
    }
}