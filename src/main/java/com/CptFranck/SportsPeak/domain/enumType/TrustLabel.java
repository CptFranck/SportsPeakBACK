package com.CptFranck.SportsPeak.domain.enumType;

import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;

public enum TrustLabel {
    UNVERIFIED("Uncertified"),
    TRUSTED("Trusted"),
    EXPERT_APPROVED("Expert Approved");

    public final String label;

    TrustLabel(String label) {
        this.label = label;
    }

    public static TrustLabel valueOfLabel(String label) {
        for (TrustLabel e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new LabelMatchNotFoundException("VisibilityLabel", label);
    }
}
