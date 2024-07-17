package com.CptFranck.SportsPeak.domain.input.targetSet;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InputNewTargetSet {

    private Integer index;

    private Integer setNumber;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private InputDuration physicalExertionUnitTime;

    private InputDuration restTime;

    private LocalDateTime creationDate;

    private Long progExerciseId;

    private Long targetSetUpdateId;

}
