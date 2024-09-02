package com.CptFranck.SportsPeak.domain.input.targetSet;

import lombok.Getter;

@Getter
public class InputTargetSetState {

    private final Long id;

    private final String state;

    public InputTargetSetState(Long id, String label) {
        this.id = id;
        this.state = label;
    }
}
