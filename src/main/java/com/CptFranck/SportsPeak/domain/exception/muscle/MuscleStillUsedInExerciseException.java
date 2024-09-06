package com.CptFranck.SportsPeak.domain.exception.muscle;

public class MuscleStillUsedInExerciseException extends RuntimeException {
    public MuscleStillUsedInExerciseException(Long muscleId, Long exercises) {
        super(String.format("The muscle with id %s always used by %s exercise(s)", muscleId.toString(), exercises.toString()));
    }
}
