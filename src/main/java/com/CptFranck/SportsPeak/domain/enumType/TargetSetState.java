package com.CptFranck.SportsPeak.domain.enumType;

import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;

public enum TargetSetState {
    USED("Used"),
    UNUSED("Unused"),
    HIDDEN("Hidden");

    public final String label;

    TargetSetState(String label) {
        this.label = label;
    }

    public static TargetSetState valueOfLabel(String label) {
        for (TargetSetState e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new LabelMatchNotFoundException("VisibilityLabel", label);
    }
}
