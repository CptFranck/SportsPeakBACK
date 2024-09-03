package com.CptFranck.SportsPeak.domain.input.progExercise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputProgExercise {

    private Long id;

    private String name;

    private String note;

    private String visibility;

    private Long exerciseId;
}
