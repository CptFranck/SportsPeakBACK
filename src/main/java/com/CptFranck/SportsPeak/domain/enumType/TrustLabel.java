package com.CptFranck.SportsPeak.domain.enumType;

public enum TrustLabel {
    UNVERIFIED("Uncertified"),
    TRUSTED("Trusted"),
    EXPERT_APPROVED("Expert_Approved");

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
        return null;
    }
}
