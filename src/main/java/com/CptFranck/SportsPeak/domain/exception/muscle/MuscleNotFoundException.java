package com.CptFranck.SportsPeak.domain.exception.muscle;

public class MuscleNotFoundException extends RuntimeException {
    public MuscleNotFoundException(Long muscleId) {
        super(String.format("The muscle with id %s has not been found", muscleId.toString()));
    }
}
