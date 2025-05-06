package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ExerciseInputResolver {

    private final MuscleService muscleService;

    private final ExerciseService exerciseService;

    private final ExerciseTypeService exerciseTypeService;

    public ExerciseInputResolver(MuscleService muscleService, ExerciseTypeService exerciseTypeService, ExerciseService exerciseService) {
        this.muscleService = muscleService;
        this.exerciseService = exerciseService;
        this.exerciseTypeService = exerciseTypeService;
    }

    public ExerciseEntity resolveInput(InputNewExercise input) {
        Set<Long> muscleIds = Sets.newHashSet(input.getMuscleIds());
        Set<Long> exerciseTypeIds = Sets.newHashSet(input.getExerciseTypeIds());

        Set<MuscleEntity> muscles = muscleService.findMany(muscleIds);
        Set<ExerciseTypeEntity> exerciseTypes = exerciseTypeService.findMany(exerciseTypeIds);

        Long id;
        Set<ProgExerciseEntity> progExercises;
        if (input instanceof InputExercise) {
            id = ((InputExercise) input).getId();
            progExercises = exerciseService.findOne(id).getProgExercises();
        } else {
            id = null;
            progExercises = new HashSet<>();
        }

        return new ExerciseEntity(
                id,
                input.getName(),
                input.getDescription(),
                input.getGoal(),
                muscles,
                exerciseTypes,
                progExercises
        );
    }
}
