package com.CptFranck.SportsPeak.domain.exception.progExercise;

public class ProgExerciseNotFoundException extends RuntimeException {
    public ProgExerciseNotFoundException(Long id) {
        super(String.format("The progExercise with the id %s has not been found", id.toString()));
    }
}
