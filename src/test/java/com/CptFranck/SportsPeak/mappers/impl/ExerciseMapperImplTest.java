package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.*;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.TestDataExerciseTypeUtils.createTestExerciseTypeDto;
import static com.CptFranck.SportsPeak.domain.TestDataExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.TestDataExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.TestDataMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.TestDataMuscleUtils.createTestMuscleDto;
import static com.CptFranck.SportsPeak.domain.TestDataProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.TestDataProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.TestDataUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.TestDataUserUtils.createTestUserDto;

@ExtendWith(MockitoExtension.class)
public class ExerciseMapperImplTest {

    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseMapperImplTest() {
        this.exerciseMapper = new ExerciseMapperImpl(new ModelMapper());
    }

    @Test
    void testExerciseTypeMapper_MapTo_Success() {
        ExerciseEntity exercise = createTestExercise();
        MuscleEntity muscle = createTestMuscle();
        ExerciseTypeEntity exerciseType = createTestExerciseType();
        UserEntity user = createTestUser();
        ProgExerciseEntity progExercise = createTestProgExercise(user, exercise);

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
    void testExerciseTypeMapper_MapFrom_Success() {
        ExerciseDto exercise = createTestExerciseDto();
        MuscleDto muscle = createTestMuscleDto();
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
