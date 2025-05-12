package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.service.ExerciseManager;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExerciseManagerImpl implements ExerciseManager {

    private final MuscleService muscleService;

    private final ExerciseService exerciseService;

    private final ExerciseTypeService exerciseTypeService;

    public ExerciseManagerImpl(MuscleService muscleService, ExerciseService exerciseService, ExerciseTypeService exerciseTypeService) {
        this.muscleService = muscleService;
        this.exerciseService = exerciseService;
        this.exerciseTypeService = exerciseTypeService;
    }

    @Override
    public ExerciseTypeEntity saveExerciseType(ExerciseTypeEntity exerciseType) {
        Set<Long> oldExerciseType;
        if (exerciseType.getId() == null)
            oldExerciseType = Collections.emptySet();
        else
            oldExerciseType = exerciseTypeService.findOne(exerciseType.getId()).getExercises()
                    .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        Set<Long> newExerciseType = exerciseType.getExercises()
                .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeService.save(exerciseType);

        exerciseService.updateExerciseTypeRelation(
                newExerciseType,
                oldExerciseType,
                exerciseTypeSaved);

        return exerciseTypeSaved;
    }

    @Override
    public MuscleEntity saveMuscle(MuscleEntity muscle) {
        Set<Long> oldExerciseIds;
        if (muscle.getId() == null)
            oldExerciseIds = Collections.emptySet();
        else
            oldExerciseIds = muscleService.findOne(muscle.getId()).getExercises()
                    .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());
        Set<Long> newExerciseIds = muscle.getExercises()
                .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        MuscleEntity muscleSaved = muscleService.save(muscle);

        exerciseService.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscleSaved);

        return muscleSaved;
    }
}
