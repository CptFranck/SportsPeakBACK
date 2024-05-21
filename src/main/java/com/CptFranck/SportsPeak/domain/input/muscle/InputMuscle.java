package com.CptFranck.SportsPeak.domain.input.muscle;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputMuscle extends InputNewMuscle {

    private Long id;

}
