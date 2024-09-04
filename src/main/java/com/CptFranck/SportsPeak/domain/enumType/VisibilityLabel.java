package com.CptFranck.SportsPeak.domain.enumType;

import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;

public enum VisibilityLabel {
    PRIVATE("Private"),
    PUBLIC("Public");

    public final String label;

    VisibilityLabel(String label) {
        this.label = label;
    }

    public static VisibilityLabel valueOfLabel(String label) {
        for (VisibilityLabel e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new LabelMatchNotFoundException("VisibilityLabel", label);
    }
}
