package com.CptFranck.SportsPeak.domain.exception.progExercise;

public class ProgExercisePublicException extends RuntimeException {
    public ProgExercisePublicException(Long id) {
        super(String.format("The progExercise with the id %s still in public, set it to private first before delete it", id.toString()));
    }
}
