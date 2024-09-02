package com.CptFranck.SportsPeak.domain.input.exerciseType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewExerciseType {

    private String name;

    private String goal;

    private List<Long> exerciseIds;
}
