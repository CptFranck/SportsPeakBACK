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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscleDto;

@ExtendWith(MockitoExtension.class)
public class MuscleMapperImplTest {

    private final Mapper<MuscleEntity, MuscleDto> muscleMapper;

    public MuscleMapperImplTest() {
        this.muscleMapper = new MuscleMapperImpl(new ModelMapper());
    }

    @Test
    void muscleMapper_MapTo_Success() {
        MuscleEntity muscle = createTestMuscle(1L);
        ExerciseEntity exercise = createTestExercise(1L);

        muscle.getExercises().add(exercise);
        MuscleDto muscleDto = muscleMapper.mapTo(muscle);

        Assertions.assertEquals(muscle.getId(), muscleDto.getId());
        Assertions.assertEquals(muscle.getName(), muscleDto.getName());
        Assertions.assertEquals(muscle.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(muscle.getExercises().size(), muscleDto.getExercises().size());
        Assertions.assertArrayEquals(
                muscle.getExercises().stream().map(ExerciseEntity::getId).toArray(),
                muscleDto.getExercises().stream().map(ExerciseDto::getId).toArray()
        );
    }

    @Test
    void muscleMapper_MapFrom_Success() {
        MuscleDto muscle = createTestMuscleDto(1L);
        ExerciseDto exercise = createTestExerciseDto(1L);

        muscle.getExercises().add(exercise);
        MuscleEntity muscleEntity = muscleMapper.mapFrom(muscle);

        Assertions.assertEquals(muscle.getId(), muscleEntity.getId());
        Assertions.assertEquals(muscle.getName(), muscleEntity.getName());
        Assertions.assertEquals(muscle.getLatinName(), muscleEntity.getLatinName());
        Assertions.assertEquals(muscle.getFunction(), muscleEntity.getFunction());
        Assertions.assertEquals(muscle.getExercises().size(), muscleEntity.getExercises().size());
        Assertions.assertArrayEquals(
                muscle.getExercises().stream().map(ExerciseDto::getId).toArray(),
                muscleEntity.getExercises().stream().map(ExerciseEntity::getId).toArray()
        );
    }

}
