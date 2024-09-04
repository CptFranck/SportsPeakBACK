package com.CptFranck.SportsPeak.domain.enumType;

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
        return null;
    }
}
