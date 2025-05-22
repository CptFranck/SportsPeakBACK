package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.mapper.impl.TargetSetMapperImpl;
import com.CptFranck.SportsPeak.utils.TargetSetTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestPerformanceLogDto;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSetDto;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;

public class TargetSetMapperImplIT {

    private final Mapper<TargetSetEntity, TargetSetDto> targetSetMapper;

    public TargetSetMapperImplIT() {
        this.targetSetMapper = new TargetSetMapperImpl(new ModelMapper());
    }

    @Test
    void targetSetTypeMapper_MapTo_WithoutUpdate_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);

        TargetSetDto targetSetDto = targetSetMapper.mapTo(targetSet);

        targetSetCommonAssertEquals(targetSet, targetSetDto);
        Assertions.assertNull(targetSetDto.getTargetSetUpdate());
    }

    @Test
    void targetSetTypeMapper_MapTo_WithUpdate_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        targetSet.getPerformanceLogs().add(performanceLog);
        TargetSetEntity targetSetUpdate = TargetSetTestUtils.createTestTargetSet(1L, progExercise, null);
        targetSet.setTargetSetUpdate(targetSetUpdate);

        TargetSetDto targetSetDto = targetSetMapper.mapTo(targetSet);

        targetSetCommonAssertEquals(targetSet, targetSetDto);
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetDto.getTargetSetUpdate().getId());
    }

    @Test
    void targetSetTypeMapper_MapFrom_WithoutUpdate_Success() {
        TargetSetDto targetSet = createTestTargetSetDto(1L, null);

        TargetSetEntity targetSetEntity = targetSetMapper.mapFrom(targetSet);

        Assertions.assertEquals(targetSetEntity.getId(), targetSet.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSet.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSet.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSet.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSet.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSet.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSet.getState());
        Assertions.assertEquals(targetSetEntity.getCreationDate(), targetSet.getCreationDate());
        Assertions.assertEquals(targetSetEntity.getPerformanceLogs().size(), targetSet.getPerformanceLogs().size());
        Assertions.assertArrayEquals(
                targetSetEntity.getPerformanceLogs().stream().map(PerformanceLogEntity::getId).toArray(),
                targetSet.getPerformanceLogs().stream().map(PerformanceLogDto::getId).toArray()
        );
        Assertions.assertNull(targetSetEntity.getTargetSetUpdate());
    }

    @Test
    void targetSetTypeMapper_MapFrom_WithUpdate_Success() {
        TargetSetDto targetSet = createTestTargetSetDto(1L, null);
        PerformanceLogDto performanceLog = createTestPerformanceLogDto(1L);
        targetSet.getPerformanceLogs().add(performanceLog);
        TargetSetDto targetSetUpdate = TargetSetTestUtils.createTestTargetSetDto(1L, null);
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
        Assertions.assertEquals(targetSet.getPerformanceLogs().size(), targetSetDto.getPerformanceLogs().size());
        Assertions.assertArrayEquals(
                targetSet.getPerformanceLogs().stream().map(PerformanceLogEntity::getId).toArray(),
                targetSetDto.getPerformanceLogs().stream().map(PerformanceLogDto::getId).toArray()
        );
    }
}
