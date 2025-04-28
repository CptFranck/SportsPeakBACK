package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.mappers.impl.ExerciseTypeMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseTypeDto;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;

@ExtendWith(MockitoExtension.class)
public class ExerciseTypeMapperImplTest {

    private final Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    public ExerciseTypeMapperImplTest() {
        this.exerciseTypeMapper = new ExerciseTypeMapperImpl(new ModelMapper());
    }

    @Test
    void exerciseTypeMapper_MapTo_Success() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        exerciseType.getExercises().add(exercise);

        ExerciseTypeDto exerciseTypeDto = exerciseTypeMapper.mapTo(exerciseType);

        Assertions.assertEquals(exerciseType.getId(), exerciseTypeDto.getId());
        Assertions.assertEquals(exerciseType.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(exerciseType.getGoal(), exerciseTypeDto.getGoal());
        Assertions.assertEquals(exerciseType.getExercises().size(), exerciseTypeDto.getExercises().size());
        Assertions.assertArrayEquals(
                exerciseType.getExercises().stream().map(ExerciseEntity::getId).toArray(),
                exerciseTypeDto.getExercises().stream().map(ExerciseDto::getId).toArray()
        );
    }

    @Test
    void exerciseTypeMapper_FromTo_Success() {
        ExerciseTypeDto exerciseType = createTestExerciseTypeDto(1L);
        ExerciseDto exercise = createTestExerciseDto(1L);
        exerciseType.getExercises().add(exercise);

        ExerciseTypeEntity exerciseTypeEntity = exerciseTypeMapper.mapFrom(exerciseType);

        Assertions.assertEquals(exerciseType.getId(), exerciseTypeEntity.getId());
        Assertions.assertEquals(exerciseType.getName(), exerciseTypeEntity.getName());
        Assertions.assertEquals(exerciseType.getGoal(), exerciseTypeEntity.getGoal());
        Assertions.assertEquals(exerciseType.getExercises().size(), exerciseTypeEntity.getExercises().size());
        Assertions.assertArrayEquals(
                exerciseType.getExercises().stream().map(ExerciseDto::getId).toArray(),
                exerciseTypeEntity.getExercises().stream().map(ExerciseEntity::getId).toArray()
        );
    }
}
