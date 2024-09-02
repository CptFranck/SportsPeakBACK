package com.CptFranck.SportsPeak.domain.input.targetSet;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = true)
public class InputNewTargetSet extends TargetSetInputBase {

    private final LocalDateTime creationDate;

    private final Long progExerciseId;

    private final Long targetSetUpdateId;


    public InputNewTargetSet(
            Integer index,
            Integer setNumber,
            Integer repetitionNumber,
            Float weight,
            String weightUnit,
            InputDuration physicalExertionUnitTime,
            InputDuration restTime,
            LocalDateTime creationDate,
            Long progExerciseId,
            Long targetSetUpdateId
    ) {
        super(index, setNumber, repetitionNumber, weight, weightUnit, physicalExertionUnitTime, restTime);
        this.creationDate = creationDate;
        this.progExerciseId = progExerciseId;
        this.targetSetUpdateId = targetSetUpdateId;
    }
}
