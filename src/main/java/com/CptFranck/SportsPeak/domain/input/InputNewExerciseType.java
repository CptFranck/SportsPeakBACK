package com.CptFranck.SportsPeak.domain.input;

import lombok.Data;

import java.util.List;


@Data
public class InputNewExerciseType {

    private String name;

    private String goal;

    private List<Long> exerciseIds;
}
