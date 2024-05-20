package com.CptFranck.SportsPeak.domain.input;

import lombok.Data;

import java.util.List;

@Data
public class InputNewMuscle {

    private String name;

    private String function;

    private List<Long> exerciseIds;
}
