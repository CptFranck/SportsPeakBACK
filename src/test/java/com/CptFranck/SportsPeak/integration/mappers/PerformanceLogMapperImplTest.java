package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.mappers.impl.PerformanceLogMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLogDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;

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
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);

        PerformanceLogDto performanceLogDto = performanceLogMapper.mapTo(performanceLog);

        Assertions.assertEquals(performanceLog.getId(), performanceLogDto.getId());
        Assertions.assertEquals(performanceLog.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(performanceLog.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(performanceLog.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(performanceLog.getWeightUnit().label, performanceLogDto.getWeightUnit());
        Assertions.assertEquals(performanceLog.getLogDate(), performanceLogDto.getLogDate());
    }

    @Test
    void performanceLogTypeMapper_MapFrom_WithoutUpdate_Success() {
        PerformanceLogDto performanceLog = createTestPerformanceLogDto(1L);

        PerformanceLogEntity performanceLogEntity = performanceLogMapper.mapFrom(performanceLog);

        Assertions.assertEquals(performanceLog.getId(), performanceLogEntity.getId());
        Assertions.assertEquals(performanceLog.getSetIndex(), performanceLogEntity.getSetIndex());
        Assertions.assertEquals(performanceLog.getRepetitionNumber(), performanceLogEntity.getRepetitionNumber());
        Assertions.assertEquals(performanceLog.getWeight(), performanceLogEntity.getWeight());
        Assertions.assertEquals(performanceLog.getWeightUnit(), performanceLogEntity.getWeightUnit().label);
        Assertions.assertEquals(performanceLog.getLogDate(), performanceLogEntity.getLogDate());
    }
}
