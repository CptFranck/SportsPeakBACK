package com.CptFranck.SportsPeak.domain.input.muscle;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class InputMuscle extends InputNewMuscle {

    private Long id;

    public InputMuscle(Long id, String name, String function, String description, ArrayList<Long> exerciseIds) {
        super(name, function, description, exerciseIds);
        this.id = id;
    }
}
