package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
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

    private String state;

    private ProgExerciseDto progExercise;

    private TargetSetDto targetSetUpdate;

    private Set<PerformanceLogDto> performanceLogs;
}
