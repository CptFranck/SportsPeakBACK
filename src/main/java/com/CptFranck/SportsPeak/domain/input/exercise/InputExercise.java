package com.CptFranck.SportsPeak.domain.input.exercise;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputExercise extends InputNewExercise {

    private Long id;

}
