package com.CptFranck.SportsPeak.domain.input.exercise;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputExercise extends InputNewExercise {

    private Long id;

    public InputExercise(Long id, String name, String description, String goal, List<Long> muscleIds, List<Long> exerciseTypeIds) {
        super(name, description, goal, muscleIds, exerciseTypeIds);
        this.id = id;
    }
}
