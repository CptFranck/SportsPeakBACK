package com.CptFranck.SportsPeak.domain.input.exercise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewExercise {

    private String name;

    private String description;

    private String goal;

    private List<Long> muscleIds;

    private List<Long> exerciseTypeIds;
}
