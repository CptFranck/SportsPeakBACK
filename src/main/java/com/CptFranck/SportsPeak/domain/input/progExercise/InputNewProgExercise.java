package com.CptFranck.SportsPeak.domain.input.progExercise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewProgExercise {

    private String name;

    private String note;

    private String visibility;

    private Long creatorId;

    private Long exerciseId;

}
