package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.mapper.impl.ProgExerciseMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSetDto;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserDto;

public class ProgExerciseMapperImplIntTest {

    private final Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    public ProgExerciseMapperImplIntTest() {
        this.progExerciseMapper = new ProgExerciseMapperImpl(new ModelMapper());
    }

    @Test
    void progExerciseTypeMapper_MapTo_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);

        progExercise.getTargetSets().add(targetSet);
        progExercise.getSubscribedUsers().add(user);

        ProgExerciseDto progExerciseDto = progExerciseMapper.mapTo(progExercise);

        Assertions.assertEquals(progExercise.getId(), progExerciseDto.getId());
        Assertions.assertEquals(progExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDto.getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDto.getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDto.getTargetSets().size());
        Assertions.assertArrayEquals(
                progExercise.getTargetSets().stream().map(TargetSetEntity::getId).toArray(),
                progExerciseDto.getTargetSets().stream().map(TargetSetDto::getId).toArray()
        );
    }

    @Test
    void progExerciseTypeMapper_MapFrom_Success() {
        UserDto user = createTestUserDto(1L);
        ExerciseDto exercise = createTestExerciseDto(1L);
        ProgExerciseDto progExercise = createTestProgExerciseDto(1L, user, exercise);
        TargetSetDto targetSet = createTestTargetSetDto(1L, null);

        progExercise.getTargetSets().add(targetSet);

        ProgExerciseEntity progExerciseEntity = progExerciseMapper.mapFrom(progExercise);

        Assertions.assertEquals(progExercise.getId(), progExerciseEntity.getId());
        Assertions.assertEquals(progExercise.getName(), progExerciseEntity.getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseEntity.getNote());
        Assertions.assertEquals(progExercise.getVisibility(), progExerciseEntity.getVisibility().label);
        Assertions.assertEquals(progExercise.getTrustLabel(), progExerciseEntity.getTrustLabel().label);
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseEntity.getCreator().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseEntity.getTargetSets().size());
        Assertions.assertArrayEquals(
                progExercise.getTargetSets().stream().map(TargetSetDto::getId).toArray(),
                progExerciseEntity.getTargetSets().stream().map(TargetSetEntity::getId).toArray()
        );
    }
}
