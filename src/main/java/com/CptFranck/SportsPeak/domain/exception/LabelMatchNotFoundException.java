package com.CptFranck.SportsPeak.domain.exception;

public class LabelMatchNotFoundException extends RuntimeException {
    public LabelMatchNotFoundException(String labelName, String labelGiven) {
        super(String.format("No %s (label) match with %s", labelName, labelGiven));
    }
}
