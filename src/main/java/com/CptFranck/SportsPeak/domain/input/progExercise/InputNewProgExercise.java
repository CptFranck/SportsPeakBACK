package com.CptFranck.SportsPeak.domain.input.progExercise;

import lombok.Data;

@Data
public class InputNewProgExercise {

    private String name;

    private String note;

    private String visibility;

    private Long creatorId;

    private Long exerciseId;

}
