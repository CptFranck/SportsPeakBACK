package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.mapper.impl.MuscleMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscleDto;

public class MuscleMapperImplIntTest {

    private final Mapper<MuscleEntity, MuscleDto> muscleMapper;

    public MuscleMapperImplIntTest() {
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
