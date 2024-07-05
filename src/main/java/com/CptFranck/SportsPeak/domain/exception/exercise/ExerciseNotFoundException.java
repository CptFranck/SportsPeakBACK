package com.CptFranck.SportsPeak.domain.exception.exercise;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(Long id) {
        super(String.format("The exercise with the id %s has not been found", id.toString()));
    }
}
