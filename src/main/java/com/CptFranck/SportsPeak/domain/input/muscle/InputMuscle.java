package com.CptFranck.SportsPeak.domain.input.muscle;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class InputMuscle extends InputNewMuscle {

    private Long id;

    public InputMuscle(Long id, String name, String latinName, String function, String description, ArrayList<Long> exerciseIds) {
        super(name, latinName, function, description, exerciseIds);
        this.id = id;
    }
}
