package com.CptFranck.SportsPeak.domain.input.performanceLog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewPerformanceLog {

    private Integer setIndex;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private LocalDateTime logDate;

    private Long targetSetId;

}
