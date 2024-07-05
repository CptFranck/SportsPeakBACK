package com.CptFranck.SportsPeak.domain.input.targetSet;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class InputNewTargetSet {

    private Integer setNumber;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private Duration physicalExertionUnitTime;

    private Duration restTime;

    private LocalDate creationDate;

    private Long progExerciseId;

    private Long targetSetUpdateId;

}
