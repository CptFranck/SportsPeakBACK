package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ExerciseInputResolver {
    private final MuscleService muscleService;
    private final ExerciseTypeService exerciseTypeService;

    public ExerciseInputResolver(MuscleService muscleService, ExerciseTypeService exerciseTypeService) {
        this.muscleService = muscleService;
        this.exerciseTypeService = exerciseTypeService;
    }

    public ExerciseEntity resolveInput(InputNewExercise input) {
        Set<Long> muscleIds = Sets.newHashSet(input.getMuscleIds());
        Set<Long> exerciseTypeIds = Sets.newHashSet(input.getExerciseTypeIds());

        Set<MuscleEntity> muscles = muscleService.findMany(muscleIds);
        Set<ExerciseTypeEntity> exerciseTypes = exerciseTypeService.findMany(exerciseTypeIds);

        return new ExerciseEntity(
                null,
                input.getName(),
                input.getDescription(),
                input.getGoal(),
                muscles,
                exerciseTypes,
                new HashSet<>()
        );
    }

    public ExerciseEntity resolveInput(InputExercise input, ExerciseEntity existingEntity) {
        ExerciseEntity base = resolveInput(input);

        base.setId(input.getId());
        base.setProgExercises(existingEntity.getProgExercises());

        return base;
    }
}
