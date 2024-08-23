package com.CptFranck.SportsPeak.domain.exception.exercise;

public class ExerciseTypeNotFoundException extends RuntimeException {
    public ExerciseTypeNotFoundException(Long id) {
        super(String.format("The exercise with the id %s has not been found", id.toString()));
    }
}
