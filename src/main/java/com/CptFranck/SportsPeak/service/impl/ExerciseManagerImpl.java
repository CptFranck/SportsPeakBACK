package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.service.ExerciseManager;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExerciseManagerImpl implements ExerciseManager {

    private final ExerciseService exerciseService;

    private final ExerciseTypeService exerciseTypeService;

    public ExerciseManagerImpl(ExerciseService exerciseService, ExerciseTypeService exerciseTypeService) {
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
}
