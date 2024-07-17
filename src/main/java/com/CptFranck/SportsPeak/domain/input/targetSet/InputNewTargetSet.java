package com.CptFranck.SportsPeak.domain.input.targetSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputNewTargetSet extends AbstractInputNewTargetSet {

    private LocalDateTime creationDate;

    private Long progExerciseId;

    private Long targetSetUpdateId;
}
