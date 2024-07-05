package com.CptFranck.SportsPeak.domain.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetSetDto {

    private Long id;

    private Integer setNumber;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private Duration physicalExertionUnitTime;

    private Duration restTime;

    private LocalDate creationDate;

    private ProgExerciseDto progExercise;

    private TargetSetDto targetExerciseSetUpdate;

    @EqualsAndHashCode.Exclude
    private Set<PerformanceLogDto> performanceLogs;
}
