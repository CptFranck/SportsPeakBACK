package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

public interface ExerciseManager {

    ExerciseTypeEntity saveExerciseType(ExerciseTypeEntity exerciseType);

    MuscleEntity saveMuscle(MuscleEntity muscleEntity);

}
