package com.CptFranck.SportsPeak.domain.input.exerciseType;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class InputExerciseType extends InputNewExerciseType {

    private Long id;

}
