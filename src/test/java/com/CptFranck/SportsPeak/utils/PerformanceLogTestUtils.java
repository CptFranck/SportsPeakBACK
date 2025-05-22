package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.List;

import static com.CptFranck.SportsPeak.utils.DateTimeTestUtils.assertDatetimeWithTimestamp;

public class PerformanceLogTestUtils {

    public static PerformanceLogEntity createTestPerformanceLog(Long id, TargetSetEntity targetSet) {
        LocalDateTime creationDate = LocalDateTime.now();
        return new PerformanceLogEntity(
                id,
                1,
                5,
                0f,
                WeightUnit.KILOGRAMME,
                creationDate,
                targetSet
        );
    }

    public static List<PerformanceLogEntity> createNewTestPerformanceLogList(TargetSetEntity targetSet) {
        return List.of(
                createTestPerformanceLog(null, targetSet),
                createTestPerformanceLog(null, targetSet)
        );
    }

    public static PerformanceLogDto createTestPerformanceLogDto(Long id) {
        LocalDateTime creationDate = LocalDateTime.now();
        return new PerformanceLogDto(
                id,
                1,
                5,
                0f,
                WeightUnit.KILOGRAMME.label,
                creationDate
        );
    }

    public static InputNewPerformanceLog createTestInputNewPerformanceLog(Long targetSetId, boolean wrongLabel) {
        LocalDateTime logDate = LocalDateTime.now();
        String label = wrongLabel ? VisibilityLabel.PRIVATE.label : WeightUnit.KILOGRAMME.label;
        return new InputNewPerformanceLog(
                1,
                5,
                0f,
                label,
                logDate,
                targetSetId
        );
    }

    public static InputPerformanceLog createTestInputPerformanceLog(Long id, Long targetSetId, boolean wrongLabel) {
        LocalDateTime logDate = LocalDateTime.now();
        String label = wrongLabel ? VisibilityLabel.PRIVATE.label : WeightUnit.KILOGRAMME.label;
        return new InputPerformanceLog(
                id,
                1,
                5,
                0f,
                label,
                logDate,
                targetSetId
        );
    }

    public static void assertEqualPerformanceLog(PerformanceLogEntity expected, PerformanceLogEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getSetIndex(), actual.getSetIndex());
        Assertions.assertEquals(expected.getRepetitionNumber(), actual.getRepetitionNumber());
        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
        Assertions.assertEquals(expected.getWeightUnit(), actual.getWeightUnit());
        assertDatetimeWithTimestamp(expected.getLogDate(), actual.getLogDate());
    }

    public static void assertPerformanceLogDtoAndEntity(PerformanceLogEntity performanceLogEntity, PerformanceLogDto performanceLogDto) {
        Assertions.assertNotNull(performanceLogDto);
        Assertions.assertEquals(performanceLogEntity.getId(), performanceLogDto.getId());
        Assertions.assertEquals(performanceLogEntity.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(performanceLogEntity.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(performanceLogEntity.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(performanceLogEntity.getWeightUnit().label, performanceLogDto.getWeightUnit());
    }

    public static void assertPerformanceLogDtoAndInput(InputNewPerformanceLog inputNewPerformanceLog, PerformanceLogDto performanceLogDto) {
        Assertions.assertNotNull(performanceLogDto);
        Assertions.assertEquals(inputNewPerformanceLog.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(inputNewPerformanceLog.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewPerformanceLog.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(inputNewPerformanceLog.getWeightUnit(), performanceLogDto.getWeightUnit());
        assertDatetimeWithTimestamp(inputNewPerformanceLog.getLogDate(), performanceLogDto.getLogDate());
    }
}
