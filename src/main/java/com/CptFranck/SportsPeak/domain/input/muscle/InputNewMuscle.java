package com.CptFranck.SportsPeak.domain.input.muscle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewMuscle {

    private String name;

    private String latinName;

    private String function;

    private String description;

    private List<Long> exerciseIds;
}
