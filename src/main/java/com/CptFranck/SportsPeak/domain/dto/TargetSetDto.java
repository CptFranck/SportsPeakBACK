package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetSetDto {

    private Long id;

    private Integer index;

    private Integer setNumber;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private InputDuration physicalExertionUnitTime;

    private InputDuration restTime;

    private LocalDateTime creationDate;

    private ProgExerciseDto progExercise;

    private TargetSetDto targetSetUpdate;

    private String state;

    @EqualsAndHashCode.Exclude
    private Set<PerformanceLogDto> performanceLogs;
}
