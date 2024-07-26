package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.*;

@ExtendWith(MockitoExtension.class)
public class TargetSetMapperImplTest {

    private final Mapper<TargetSetEntity, TargetSetDto> targetSetMapper;

    public TargetSetMapperImplTest() {
        this.targetSetMapper = new TargetSetMapperImpl(new ModelMapper());
    }

    @Test
    void testProgExerciseTypeMapperMapTo_WithoutUpdate_Success() {
        UserEntity user = createTestUser();
        ExerciseEntity exercise = createTestExercise();
        ProgExerciseEntity progExercise = createTestProgExercise(user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(progExercise);

        TargetSetDto targetSetDto = targetSetMapper.mapTo(targetSet);

        targetSetCommonAssertEquals(targetSet, targetSetDto);
        Assertions.assertNull(targetSetDto.getTargetSetUpdate());
    }

    @Test
    void testProgExerciseTypeMapperMapTo_WithUpdate_Success() {
        UserEntity user = createTestUser();
        ExerciseEntity exercise = createTestExercise();
        ProgExerciseEntity progExercise = createTestProgExercise(user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(progExercise);
        TargetSetEntity targetSetUpdate = createTestTargetSetUpdate(progExercise, null);
        targetSet.setTargetSetUpdate(targetSetUpdate);

        TargetSetDto targetSetDto = targetSetMapper.mapTo(targetSet);

        targetSetCommonAssertEquals(targetSet, targetSetDto);
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetDto.getTargetSetUpdate().getId());
    }

    private void targetSetCommonAssertEquals(TargetSetEntity targetSet, TargetSetDto targetSetDto) {
        Assertions.assertEquals(targetSet.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSet.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSet.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSet.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSet.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSet.getWeightUnit().label, targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSet.getCreationDate(), targetSetDto.getCreationDate());
        Assertions.assertEquals(targetSet.getProgExercise().getId(), targetSetDto.getProgExercise().getId());
        Assertions.assertEquals(targetSet.getPerformanceLogs().size(), targetSetDto.getPerformanceLogs().size());
        Assertions.assertArrayEquals(
                targetSet.getPerformanceLogs().stream().map(PerformanceLogEntity::getId).toArray(),
                targetSetDto.getPerformanceLogs().stream().map(PerformanceLogDto::getId).toArray()
        );
    }
}
