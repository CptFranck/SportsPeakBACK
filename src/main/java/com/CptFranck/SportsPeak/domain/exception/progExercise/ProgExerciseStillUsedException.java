package com.CptFranck.SportsPeak.domain.exception.progExercise;

public class ProgExerciseStillUsedException extends RuntimeException {
    public ProgExerciseStillUsedException(Long id) {
        super(String.format("The progExercise with the id %s still used by some user", id.toString()));
    }
}
