package com.CptFranck.SportsPeak.domain.exception.exerciseType;

public class ExerciseTypeNotFoundException extends RuntimeException {
    public ExerciseTypeNotFoundException(Long id) {
        super(String.format("The exerciseType with the id %s has not been found", id.toString()));
    }
}
