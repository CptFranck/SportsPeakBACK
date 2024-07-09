package com.CptFranck.SportsPeak.domain.enumType;

public enum Visibility {
    PRIVATE("Private"),
    PUBLIC("Public");

    public final String label;

    Visibility(String label) {
        this.label = label;
    }

    public static Visibility valueOfLabel(String label) {
        for (Visibility e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
