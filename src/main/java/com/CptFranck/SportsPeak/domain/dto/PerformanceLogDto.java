package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceLogDto {
    private Long id;

    private Integer setIndex;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private LocalDate logDate;

    private TargetSetDto targetExerciseSet;
}
