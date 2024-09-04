package com.CptFranck.SportsPeak.domain.input.performanceLog;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class InputPerformanceLog extends InputNewPerformanceLog {

    private Long id;

    public InputPerformanceLog(
            Long id,
            Integer setIndex,
            Integer repetitionNumber,
            Float weight,
            String weightUnit,
            LocalDateTime logDate,
            Long targetSetId
    ) {
        super(setIndex, repetitionNumber, weight, weightUnit, logDate, targetSetId);
        this.id = id;
    }
}
