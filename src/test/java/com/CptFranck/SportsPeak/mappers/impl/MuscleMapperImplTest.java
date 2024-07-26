package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createTestExercise;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createTestMuscle;

@ExtendWith(MockitoExtension.class)
public class MuscleMapperImplTest {

    private final Mapper<MuscleEntity, MuscleDto> exerciseTypeMapper;

    public MuscleMapperImplTest() {
        this.exerciseTypeMapper = new MuscleMapperImpl(new ModelMapper());
    }

    @Test
    void testExerciseTypeMapperMapTo_Success() {
        MuscleEntity muscle = createTestMuscle();
        ExerciseEntity exercise = createTestExercise();

        muscle.getExercises().add(exercise);
        MuscleDto muscleDto = exerciseTypeMapper.mapTo(muscle);

        Assertions.assertEquals(muscle.getId(), muscleDto.getId());
        Assertions.assertEquals(muscle.getName(), muscleDto.getName());
        Assertions.assertEquals(muscle.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(muscle.getExercises().size(), muscleDto.getExercises().size());
        Assertions.assertArrayEquals(
                muscle.getExercises().stream().map(ExerciseEntity::getId).toArray(),
                muscleDto.getExercises().stream().map(ExerciseDto::getId).toArray()
        );
    }

}
