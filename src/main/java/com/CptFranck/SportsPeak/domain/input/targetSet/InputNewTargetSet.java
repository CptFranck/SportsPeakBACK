package com.CptFranck.SportsPeak.domain.input.targetSet;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class InputNewTargetSet extends AbstractTargetSetInput {

    private LocalDateTime creationDate;

    private Long progExerciseId;

    private Long targetSetUpdateId;

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
