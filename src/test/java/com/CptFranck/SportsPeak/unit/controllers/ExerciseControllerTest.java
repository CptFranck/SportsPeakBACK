package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.ExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
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

import java.util.LinkedHashMap;
import java.util.List;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseControllerTest {

    @InjectMocks
    private ExerciseController exerciseController;

    @Mock
    private Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private ExerciseTypeService exerciseTypeService;

    private ExerciseEntity exercise;
    private ExerciseDto exerciseDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        exercise = createTestExercise(1L);
        exerciseDto = createTestExerciseDto(1L);
    }

    @Test
    void getExercises_ValidUse_ReturnExerciseDtos() {
        when(exerciseService.findAll()).thenReturn(List.of(exercise));
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        List<ExerciseDto> exerciseDtos = exerciseController.getExercises();

        Assertions.assertEquals(List.of(exerciseDto), exerciseDtos);
    }

    @Test
    void getExerciseById_ValidInput_ReturnExerciseDto() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.getExerciseById(1L);

        Assertions.assertEquals(this.exerciseDto, exerciseDto);
    }

    @Test
    void addExercise_ValidInput_ReturnExerciseDto() {
        when(exerciseService.create(Mockito.any(InputNewExercise.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.addExercise(createTestInputNewExercise());

        Assertions.assertEquals(this.exerciseDto, exerciseDto);
    }

    @Test
    void modifyExercise_ValidInput_ReturnExerciseDto() {
        when(exerciseService.update(Mockito.any(InputExercise.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.modifyExercise(createTestInputExercise(1L));

        Assertions.assertEquals(this.exerciseDto, exerciseDto);
    }

    @Test
    void DeleteExercise_ValidInput_ReturnExerciseId() {
        Long id = exerciseController.deleteExercise(1L);

        Assertions.assertEquals(1L, id);
    }
}