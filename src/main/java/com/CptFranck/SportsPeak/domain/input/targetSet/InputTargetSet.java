package com.CptFranck.SportsPeak.domain.input.targetSet;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputTargetSet extends AbstractTargetSetInput {

    private Long id;

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
