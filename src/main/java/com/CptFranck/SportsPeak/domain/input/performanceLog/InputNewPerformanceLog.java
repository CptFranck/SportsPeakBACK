package com.CptFranck.SportsPeak.domain.input.performanceLog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InputNewPerformanceLog {

    private Integer setIndex;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private LocalDateTime logDate;

    private Long targetSetId;

}
