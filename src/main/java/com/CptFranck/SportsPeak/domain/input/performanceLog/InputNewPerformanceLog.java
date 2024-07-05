package com.CptFranck.SportsPeak.domain.input.performanceLog;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InputNewPerformanceLog {

    private Integer setIndex;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private LocalDate logDate;

    private Long targetSetId;

}
