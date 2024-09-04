package com.CptFranck.SportsPeak.domain.input.exerciseType;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Getter
@NoArgsConstructor
public class InputExerciseType extends InputNewExerciseType {

    private Long id;

    public InputExerciseType(Long id, String name, String goal, ArrayList<Long> exerciseIds) {
        super(name, goal, exerciseIds);
        this.id = id;
    }
}
