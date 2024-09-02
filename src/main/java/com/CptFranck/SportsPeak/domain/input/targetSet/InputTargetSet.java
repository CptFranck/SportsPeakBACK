package com.CptFranck.SportsPeak.domain.input.targetSet;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode(callSuper = true)
public class InputTargetSet extends TargetSetInputBase {

    private final Long id;

    public InputTargetSet(
            Long id,
            Integer index,
            Integer setNumber,
            Integer repetitionNumber,
            Float weight,
            String weightUnit,
            InputDuration physicalExertionUnitTime,
            InputDuration restTime
    ) {
        super(index, setNumber, repetitionNumber, weight, weightUnit, physicalExertionUnitTime, restTime);
        this.id = id;
    }
}
