package com.CptFranck.SportsPeak.domain.exception.tartgetSet;

public class TargetSetNotFoundException extends RuntimeException {
    public TargetSetNotFoundException(Long id) {
        super(String.format("The targetSet with the id %s has not been found", id.toString()));
    }
}
