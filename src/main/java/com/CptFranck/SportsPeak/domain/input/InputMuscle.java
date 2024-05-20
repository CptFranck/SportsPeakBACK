package com.CptFranck.SportsPeak.domain.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputMuscle extends InputNewMuscle {

    private Long id;

}
