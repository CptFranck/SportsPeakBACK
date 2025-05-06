package com.CptFranck.SportsPeak.domain.exception.exerciseType;

public class ExerciseTypeStillUsedInExerciseException extends RuntimeException {
    public ExerciseTypeStillUsedInExerciseException(Long exerciseTypeId, Long exercises) {
        super(String.format("The exercise type with id %s always used by %s exercise(s)", exerciseTypeId.toString(), exercises.toString()));
    }
}
