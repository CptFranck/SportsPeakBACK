package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.ExerciseTypeController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.ExerciseTypeInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseManager;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseTypeControllerTest {

    @InjectMocks
    private ExerciseTypeController exerciseTypeController;

    @Mock
    private Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    @Mock
    private ExerciseTypeInputResolver exerciseTypeInputResolver;

    @Mock
    private ExerciseTypeService exerciseTypeService;

    @Mock
    private ExerciseManager exerciseManager;

    private ExerciseTypeEntity exerciseType;
    private ExerciseTypeDto exerciseTypeDto;

    @BeforeEach
    void init() {
        exerciseType = createTestExerciseType(1L);
        exerciseTypeDto = createTestExerciseTypeDto(1L);
    }

    @Test
    void getExerciseTypes_ValidUse_ReturnListOfExerciseTypeDto() {
        when(exerciseTypeService.findAll()).thenReturn(List.of(exerciseType));
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        List<ExerciseTypeDto> exerciseTypeDtos = exerciseTypeController.getExerciseTypes();

        Assertions.assertEquals(List.of(exerciseTypeDto), exerciseTypeDtos);
    }

    @Test
    void getExerciseTypeById_ValidInput_ReturnExerciseTypeDto() {
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.getExerciseTypeById(1L);

        Assertions.assertEquals(this.exerciseTypeDto, exerciseTypeDto);
    }

    @Test
    void addExerciseType_ValidInput_ReturnExerciseTypeDto() {
        when(exerciseTypeInputResolver.resolveInput(Mockito.any(InputNewExerciseType.class))).thenReturn(this.exerciseType);
        when(exerciseManager.saveExerciseType(Mockito.any(ExerciseTypeEntity.class))).thenReturn(this.exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.addExerciseType(createTestInputNewExerciseType());

        Assertions.assertEquals(this.exerciseTypeDto, exerciseTypeDto);
    }

    @Test
    void modifyExerciseType_ValidInput_ReturnExerciseTypeDto() {
        when(exerciseTypeInputResolver.resolveInput(Mockito.any(InputExerciseType.class))).thenReturn(this.exerciseType);
        when(exerciseManager.saveExerciseType(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeController.modifyExerciseType(createTestInputExerciseType(1L));

        Assertions.assertEquals(this.exerciseTypeDto, exerciseTypeDto);
    }

    @Test
    void deleteExerciseType_ValidInput_Void() {
        Long id = exerciseTypeController.deleteExerciseType(1L);

        Assertions.assertEquals(1L, id);
    }
}