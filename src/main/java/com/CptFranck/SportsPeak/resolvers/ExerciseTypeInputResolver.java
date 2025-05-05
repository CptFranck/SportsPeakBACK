package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.service.ExerciseService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ExerciseTypeInputResolver {
    private final ExerciseService exerciseService;

    public ExerciseTypeInputResolver(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    public ExerciseTypeEntity resolveInput(InputNewExerciseType input) {
        Set<Long> newExerciseIds = Sets.newHashSet(input.getExerciseIds());

        Set<ExerciseEntity> exercises = exerciseService.findMany(newExerciseIds);

        return new ExerciseTypeEntity(
                null,
                input.getName(),
                input.getGoal(),
                exercises
        );
    }

    public ExerciseTypeEntity resolveInput(InputExerciseType input, ExerciseTypeEntity exerciseTypeEntity) {
        ExerciseTypeEntity base = resolveInput(input);

        base.setId(exerciseTypeEntity.getId());

        return base;
    }
}
