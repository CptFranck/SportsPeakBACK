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
import static com.CptFranck.SportsPeak.domain.utils.TestDataTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestDataTargetSetUtils.createTestTargetSetDto;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;

@ExtendWith(MockitoExtension.class)
public class PerformanceLogMapperImplTest {

    private final Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    public PerformanceLogMapperImplTest() {
        this.performanceLogMapper = new PerformanceLogMapperImpl(new ModelMapper());
    }

    @Test
    void performanceLogTypeMapper_MapTo_WithoutUpdate_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(progExercise);
        PerformanceLogEntity performanceLog = createTestPerformanceLog(targetSet);

        PerformanceLogDto performanceLogDto = performanceLogMapper.mapTo(performanceLog);

        Assertions.assertEquals(performanceLog.getId(), performanceLogDto.getId());
        Assertions.assertEquals(performanceLog.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(performanceLog.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(performanceLog.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(performanceLog.getWeightUnit().label, performanceLogDto.getWeightUnit());
        Assertions.assertEquals(performanceLog.getLogDate(), performanceLogDto.getLogDate());
        Assertions.assertEquals(performanceLog.getTargetSet().getId(), performanceLogDto.getTargetSet().getId());
    }

    @Test
    void performanceLogTypeMapper_MapFrom_WithoutUpdate_Success() {
        UserDto user = createTestUserDto();
        ExerciseDto exercise = createTestExerciseDto();
        ProgExerciseDto progExercise = createTestProgExerciseDto(user, exercise);
        TargetSetDto targetSet = createTestTargetSetDto(progExercise);
        PerformanceLogDto performanceLog = createTestPerformanceLogDto(targetSet);

        PerformanceLogEntity performanceLogEntity = performanceLogMapper.mapFrom(performanceLog);

        Assertions.assertEquals(performanceLog.getId(), performanceLogEntity.getId());
        Assertions.assertEquals(performanceLog.getSetIndex(), performanceLogEntity.getSetIndex());
        Assertions.assertEquals(performanceLog.getRepetitionNumber(), performanceLogEntity.getRepetitionNumber());
        Assertions.assertEquals(performanceLog.getWeight(), performanceLogEntity.getWeight());
        Assertions.assertEquals(performanceLog.getWeightUnit(), performanceLogEntity.getWeightUnit().label);
        Assertions.assertEquals(performanceLog.getLogDate(), performanceLogEntity.getLogDate());
        Assertions.assertEquals(performanceLog.getTargetSet().getId(), performanceLogEntity.getTargetSet().getId());
    }
}
