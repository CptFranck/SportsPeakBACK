package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createTestExercise;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createTestExerciseType;

@ExtendWith(MockitoExtension.class)
public class ExerciseTypeMapperImplTest {

    private final Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    public ExerciseTypeMapperImplTest() {
        this.exerciseTypeMapper = new ExerciseTypeMapperImpl(new ModelMapper());
    }

    @Test
    void testExerciseTypeMapperMapTo_Success() {
        ExerciseTypeEntity exerciseType = createTestExerciseType();
        ExerciseEntity exercise = createTestExercise();

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

}
