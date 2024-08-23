package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.*;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestDataPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.domain.utils.TestDataPerformanceLogUtils.createTestPerformanceLogDto;
import static com.CptFranck.SportsPeak.domain.utils.TestDataTargetSetUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createTestUserDto;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;

@ExtendWith(MockitoExtension.class)
public class TargetSetMapperImplTest {

    private final Mapper<TargetSetEntity, TargetSetDto> targetSetMapper;

    public TargetSetMapperImplTest() {
        this.targetSetMapper = new TargetSetMapperImpl(new ModelMapper());
    }

    @Test
    void targetSetTypeMapper_MapTo_WithoutUpdate_Success() {
        UserEntity user = createTestUser();
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(progExercise);

        TargetSetDto targetSetDto = targetSetMapper.mapTo(targetSet);

        targetSetCommonAssertEquals(targetSet, targetSetDto);
        Assertions.assertNull(targetSetDto.getTargetSetUpdate());
    }

    @Test
    void targetSetTypeMapper_MapTo_WithUpdate_Success() {
        UserEntity user = createTestUser();
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(progExercise);
        PerformanceLogEntity performanceLog = createTestPerformanceLog(targetSet);
        targetSet.getPerformanceLogs().add(performanceLog);
        TargetSetEntity targetSetUpdate = createTestTargetSetUpdate(progExercise, null);
        targetSet.setTargetSetUpdate(targetSetUpdate);

        TargetSetDto targetSetDto = targetSetMapper.mapTo(targetSet);

        targetSetCommonAssertEquals(targetSet, targetSetDto);
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetDto.getTargetSetUpdate().getId());
    }

    @Test
    void targetSetTypeMapper_MapFrom_WithoutUpdate_Success() {
        UserDto user = createTestUserDto();
        ExerciseDto exercise = createTestExerciseDto();
        ProgExerciseDto progExercise = createTestProgExerciseDto(user, exercise);
        TargetSetDto targetSet = createTestTargetSetDto(progExercise);

        TargetSetEntity targetSetEntity = targetSetMapper.mapFrom(targetSet);

        Assertions.assertEquals(targetSetEntity.getId(), targetSet.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSet.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSet.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSet.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSet.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSet.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSet.getState());
        Assertions.assertEquals(targetSetEntity.getCreationDate(), targetSet.getCreationDate());
        Assertions.assertEquals(targetSetEntity.getProgExercise().getId(), targetSet.getProgExercise().getId());
        Assertions.assertEquals(targetSetEntity.getPerformanceLogs().size(), targetSet.getPerformanceLogs().size());
        Assertions.assertArrayEquals(
                targetSetEntity.getPerformanceLogs().stream().map(PerformanceLogEntity::getId).toArray(),
                targetSet.getPerformanceLogs().stream().map(PerformanceLogDto::getId).toArray()
        );
        Assertions.assertNull(targetSetEntity.getTargetSetUpdate());
    }

    @Test
    void targetSetTypeMapper_MapFrom_WithUpdate_Success() {
        UserDto user = createTestUserDto();
        ExerciseDto exercise = createTestExerciseDto();
        ProgExerciseDto progExercise = createTestProgExerciseDto(user, exercise);
        TargetSetDto targetSet = createTestTargetSetDto(progExercise);
        PerformanceLogDto performanceLog = createTestPerformanceLogDto(targetSet);
        targetSet.getPerformanceLogs().add(performanceLog);
        TargetSetDto targetSetUpdate = createTestTargetSetUpdateDto(progExercise, null);
        targetSet.setTargetSetUpdate(targetSetUpdate);

        TargetSetEntity targetSetEntity = targetSetMapper.mapFrom(targetSet);

        Assertions.assertEquals(targetSetEntity.getId(), targetSet.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSet.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSet.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSet.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSet.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSet.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSet.getState());
        Assertions.assertEquals(targetSetEntity.getCreationDate(), targetSet.getCreationDate());
        Assertions.assertEquals(targetSetEntity.getProgExercise().getId(), targetSet.getProgExercise().getId());
        Assertions.assertEquals(targetSetEntity.getPerformanceLogs().size(), targetSet.getPerformanceLogs().size());
        Assertions.assertArrayEquals(
                targetSetEntity.getPerformanceLogs().stream().map(PerformanceLogEntity::getId).toArray(),
                targetSet.getPerformanceLogs().stream().map(PerformanceLogDto::getId).toArray()
        );
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetEntity.getTargetSetUpdate().getId());
    }

    private void targetSetCommonAssertEquals(TargetSetEntity targetSet, TargetSetDto targetSetDto) {
        Assertions.assertEquals(targetSet.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSet.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSet.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSet.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSet.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSet.getWeightUnit().label, targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSet.getState().label, targetSetDto.getState());
        Assertions.assertEquals(targetSet.getCreationDate(), targetSetDto.getCreationDate());
        Assertions.assertEquals(targetSet.getProgExercise().getId(), targetSetDto.getProgExercise().getId());
        Assertions.assertEquals(targetSet.getPerformanceLogs().size(), targetSetDto.getPerformanceLogs().size());
        Assertions.assertArrayEquals(
                targetSet.getPerformanceLogs().stream().map(PerformanceLogEntity::getId).toArray(),
                targetSetDto.getPerformanceLogs().stream().map(PerformanceLogDto::getId).toArray()
        );
    }
}
