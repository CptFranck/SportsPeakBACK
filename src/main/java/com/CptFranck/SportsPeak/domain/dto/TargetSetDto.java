package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.*;

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

    private InputDuration physicalExertionUnitTime;

    private InputDuration restTime;

    private LocalDate creationDate;

    private ProgExerciseDto progExercise;

    private TargetSetDto targetExerciseSetUpdate;

    @EqualsAndHashCode.Exclude
    private Set<PerformanceLogDto> performanceLogs;
}
