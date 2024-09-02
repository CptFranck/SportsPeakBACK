package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;

import java.time.LocalDateTime;
import java.util.List;

public class TestPerformanceLogUtils {

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

    public static PerformanceLogDto createTestPerformanceLogDto(Long id, TargetSetDto targetSet) {
        LocalDateTime creationDate = LocalDateTime.now();
        return new PerformanceLogDto(
                id,
                1,
                5,
                0f,
                WeightUnit.KILOGRAMME.label,
                creationDate,
                targetSet
        );
    }
}
