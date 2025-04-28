package com.CptFranck.SportsPeak.unit.controller;

import com.CptFranck.SportsPeak.controller.ExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
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
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseControllerTest {

    @InjectMocks
    private ExerciseController exerciseController;

    @Mock
    private Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    @Mock
    private MuscleService muscleService;

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
    void ExerciseController_GetExercises_Success() {
        when(exerciseService.findAll()).thenReturn(List.of(exercise));
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        List<ExerciseDto> exerciseDtos = exerciseController.getExercises();

        Assertions.assertEquals(List.of(exerciseDto), exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_Unsuccessful() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.getExerciseById(1L)
        );
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.getExerciseById(1L);

        Assertions.assertEquals(this.exerciseDto, exerciseDto);
    }

    @Test
    void ExerciseController_AddExercise_Success() {
        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.addExercise(createTestInputNewExercise());

        Assertions.assertEquals(this.exerciseDto, exerciseDto);
    }

    @Test
    void ExerciseController_ModifyExercise_UnsuccessfulDoesNotExist() {
        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.modifyExercise(createTestInputExercise(1L))
        );
    }

    @Test
    void ExerciseController_ModifyExercise_Success() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.modifyExercise(createTestInputExercise(1L));

        Assertions.assertEquals(this.exerciseDto, exerciseDto);
    }

    @Test
    void ExerciseController_DeleteExercise_Success() {
        Long id = exerciseController.deleteExercise(1L);

        Assertions.assertEquals(1L, id);
    }
}