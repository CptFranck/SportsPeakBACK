package com.CptFranck.SportsPeak.domain.input.exerciseType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputExerciseType extends InputNewExerciseType {

    private Long id;

    public InputExerciseType(Long id, String name, String goal, ArrayList<Long> longs) {
        super(name, goal, longs);
        this.id = id;
    }
}
