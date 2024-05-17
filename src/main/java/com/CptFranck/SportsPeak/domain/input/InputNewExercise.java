package com.CptFranck.SportsPeak.domain.input;

import lombok.Data;

import java.util.List;

@Data
public class InputNewExercise {

    private String name;

    private String description;

    private String goal;

    private List<Integer> muscleIds;

    private List<Integer> exerciseTypeIds;
}
