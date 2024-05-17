package com.CptFranck.SportsPeak.domain.input;

import lombok.Data;

import java.util.Set;

@Data
public class InputExercise {

    private Integer id;

    private String name;

    private String description;

    private String goal;

    private Set<Integer> muscleIds;

    private Set<Integer> exerciseTypeIds;
}
