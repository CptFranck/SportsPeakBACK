package com.CptFranck.SportsPeak.domain.input;

import lombok.Data;

import java.util.List;

@Data
public class InputMuscle {

    private Long id;

    private String name;

    private String function;

    private List<InputNewExercise> exercises;
}
