package com.CptFranck.SportsPeak.domain.input.progExercise;

import lombok.Data;

@Data
public class InputProgExercise {

    private Long id;

    private String name;

    private String note;

    private Long exerciseId;

    private String visibility;
}
