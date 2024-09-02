package com.CptFranck.SportsPeak.domain.input.muscle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputMuscle extends InputNewMuscle {

    private Long id;

    public InputMuscle(Long id, String name, String function, String description, ArrayList<Long> exerciseIds) {
        super(name, function, description, exerciseIds);
        this.id = id;
    }
}
