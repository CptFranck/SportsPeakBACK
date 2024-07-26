package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.*;

@ExtendWith(MockitoExtension.class)
public class PerformanceLogMapperImplTest {

    private final Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    public PerformanceLogMapperImplTest() {
        this.performanceLogMapper = new PerformanceLogMapperImpl(new ModelMapper());
    }

    @Test
    void testProgExerciseTypeMapperMapTo_WithoutUpdate_Success() {
        UserEntity user = createTestUser();
        ExerciseEntity exercise = createTestExercise();
        ProgExerciseEntity progExercise = createTestProgExercise(user, exercise);
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
}
