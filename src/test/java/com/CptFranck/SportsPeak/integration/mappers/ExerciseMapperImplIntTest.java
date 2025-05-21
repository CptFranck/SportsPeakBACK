package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.*;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.mapper.impl.ExerciseMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscleDto;
import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseTypeDto;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserDto;

public class ExerciseMapperImplIntTest {

    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseMapperImplIntTest() {
        this.exerciseMapper = new ExerciseMapperImpl(new ModelMapper());
    }

    @Test
    void exerciseMapper_MapTo_Success() {
        ExerciseEntity exercise = createTestExercise(1L);
        MuscleEntity muscle = createTestMuscle(1L);
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        UserEntity user = createTestUser(1L);
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
        ExerciseDto exercise = createTestExerciseDto(1L);
        MuscleDto muscle = createTestMuscleDto(1L);
        ExerciseTypeDto exerciseType = createTestExerciseTypeDto(1L);
        UserDto user = createTestUserDto(1L);
        ProgExerciseDto progExercise = createTestProgExerciseDto(1L, user, exercise);

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
