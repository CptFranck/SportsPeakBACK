package com.CptFranck.SportsPeak.resolver;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MuscleInputResolver {

    private final MuscleService muscleService;

    private final ExerciseService exerciseService;

    public MuscleInputResolver(MuscleService muscleService, ExerciseService exerciseService) {
        this.muscleService = muscleService;
        this.exerciseService = exerciseService;
    }

    public MuscleEntity resolveInput(InputNewMuscle inputNewMuscle) {
        Set<Long> newExerciseIds = Sets.newHashSet(inputNewMuscle.getExerciseIds());
        Set<ExerciseEntity> exercises = exerciseService.findMany(newExerciseIds);

        Long id;
        String illustrationPath;
        if (inputNewMuscle instanceof InputMuscle) {
            id = ((InputMuscle) inputNewMuscle).getId();
            illustrationPath = muscleService.findOne(id).getIllustrationPath();
        } else {
            id = null;
            illustrationPath = "";
        }

        return new MuscleEntity(
                id,
                inputNewMuscle.getName(),
                inputNewMuscle.getLatinName(),
                inputNewMuscle.getDescription(),
                inputNewMuscle.getFunction(),
                illustrationPath,
                exercises
        );
    }
}
