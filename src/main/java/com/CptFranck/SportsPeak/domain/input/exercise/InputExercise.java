package com.CptFranck.SportsPeak.domain.input.exercise;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputExercise extends InputNewExercise {

    private Long id;

    public InputExercise(String name, String description, String goal, List<Long> muscleIds, List<Long> exerciseTypeIds) {
        super(name, description, goal, muscleIds, exerciseTypeIds);
    }
}
