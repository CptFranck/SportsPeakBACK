package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.ExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
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

import java.util.*;

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

        Assertions.assertNotNull(exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_Unsuccessful() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        ExerciseDto exerciseDto = exerciseController.getExerciseById(1L);

        Assertions.assertNull(exerciseDto);
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.getExerciseById(1L);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_AddExercise_Success() {
        Set<MuscleEntity> muscles = new HashSet<>();
        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.addExercise(createTestInputNewExercise());

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_ModifyExercise_UnsuccessfulDoesNotExist() {
        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(false);

        ExerciseDto exerciseDto = exerciseController.modifyExercise(createTestInputExercise());

        Assertions.assertNull(exerciseDto);
    }

    @Test
    void ExerciseController_ModifyExercise_UnsuccessfulExerciseNotFound() {
        Set<MuscleEntity> muscles = new HashSet<>();
        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> exerciseController.modifyExercise(createTestInputExercise()));
    }

    @Test
    void ExerciseController_ModifyExercise_Success() {
        Set<MuscleEntity> muscles = new HashSet<>();
        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        ExerciseDto exerciseDto = exerciseController.modifyExercise(createTestInputExercise());

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_DeleteExercise_Success() {
        Long id = exerciseController.deleteExercise(1L);

        Assertions.assertNotNull(id);
    }
}