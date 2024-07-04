package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceLogDto {
    private Long id;

    private Integer setNumber;

    private Integer repetitionNumber;

    private Integer weight;

    private String weightUnit;

    private Duration restTime;

    private LocalDate logDate;

    private TargetExerciseSetDto targetExerciseSet;
}
