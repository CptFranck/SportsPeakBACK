package com.CptFranck.SportsPeak.domain.input.muscle;

import lombok.Data;

import java.util.List;

@Data
public class InputNewMuscle {

    private String name;

    private String function;

    private String description;

    private List<Long> exerciseIds;
}
