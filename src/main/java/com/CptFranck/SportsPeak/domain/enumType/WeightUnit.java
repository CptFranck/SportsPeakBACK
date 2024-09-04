package com.CptFranck.SportsPeak.domain.enumType;

import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;

public enum WeightUnit {
    KILOGRAMME("kgs"),
    POUND("lbs");

    public final String label;

    WeightUnit(String label) {
        this.label = label;
    }

    public static WeightUnit valueOfLabel(String label) {
        for (WeightUnit e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new LabelMatchNotFoundException("WeightUnit", label);
    }
}
