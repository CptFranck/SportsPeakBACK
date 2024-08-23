package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.*;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createTestUserDto;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseTypeDto;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscleDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;

@ExtendWith(MockitoExtension.class)
public class ExerciseMapperImplTest {

    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseMapperImplTest() {
        this.exerciseMapper = new ExerciseMapperImpl(new ModelMapper());
    }

    @Test
    void exerciseMapper_MapTo_Success() {
        ExerciseEntity exercise = createTestExercise(1L);
        MuscleEntity muscle = createTestMuscle(1L);
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        UserEntity user = createTestUser();
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);

        exercise.getMuscles().add(muscle);
        exercise.getExerciseTypes().add(exerciseType);
        exercise.getProgExercises().add(progExercise);

        ExerciseDto exerciseDto = exerciseMapper.mapTo(exercise);

        Assertions.assertEquals(exercise.getId(), exerciseDto.getId());
        Assertions.assertEquals(exercise.getName(), exerciseDto.getName());
        Assertions.assertEquals(exercise.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(exercise.getExerciseTypes().size(), exerciseDto.getExerciseTypes().size());
        Assertions.assertArrayEquals(
                exercise.getExerciseTypes().stream().map(ExerciseTypeEntity::getId).toArray(),
                exerciseDto.getExerciseTypes().stream().map(ExerciseTypeDto::getId).toArray()
        );
        Assertions.assertEquals(exercise.getMuscles().size(), exerciseDto.getMuscles().size());
        Assertions.assertArrayEquals(
                exercise.getMuscles().stream().map(MuscleEntity::getId).toArray(),
                exerciseDto.getMuscles().stream().map(MuscleDto::getId).toArray()
        );
        Assertions.assertEquals(exercise.getProgExercises().size(), exerciseDto.getProgExercises().size());
        Assertions.assertArrayEquals(
                exercise.getProgExercises().stream().map(ProgExerciseEntity::getId).toArray(),
                exerciseDto.getProgExercises().stream().map(ProgExerciseDto::getId).toArray()
        );
    }

    @Test
    void exerciseMapper_MapFrom_Success() {
        ExerciseDto exercise = createTestExerciseDto();
        MuscleDto muscle = createTestMuscleDto(1L);
        ExerciseTypeDto exerciseType = createTestExerciseTypeDto();
        UserDto user = createTestUserDto();
        ProgExerciseDto progExercise = createTestProgExerciseDto(user, exercise);

        exercise.getMuscles().add(muscle);
        exercise.getExerciseTypes().add(exerciseType);
        exercise.getProgExercises().add(progExercise);

        ExerciseEntity exerciseEntity = exerciseMapper.mapFrom(exercise);

        Assertions.assertEquals(exercise.getId(), exerciseEntity.getId());
        Assertions.assertEquals(exercise.getName(), exerciseEntity.getName());
        Assertions.assertEquals(exercise.getGoal(), exerciseEntity.getGoal());
        Assertions.assertEquals(exercise.getExerciseTypes().size(), exerciseEntity.getExerciseTypes().size());
        Assertions.assertArrayEquals(
                exercise.getExerciseTypes().stream().map(ExerciseTypeDto::getId).toArray(),
                exerciseEntity.getExerciseTypes().stream().map(ExerciseTypeEntity::getId).toArray()
        );
        Assertions.assertEquals(exercise.getMuscles().size(), exerciseEntity.getMuscles().size());
        Assertions.assertArrayEquals(
                exercise.getMuscles().stream().map(MuscleDto::getId).toArray(),
                exerciseEntity.getMuscles().stream().map(MuscleEntity::getId).toArray()
        );
        Assertions.assertEquals(exercise.getProgExercises().size(), exerciseEntity.getProgExercises().size());
        Assertions.assertArrayEquals(
                exercise.getProgExercises().stream().map(ProgExerciseDto::getId).toArray(),
                exerciseEntity.getProgExercises().stream().map(ProgExerciseEntity::getId).toArray()
        );
    }
}
