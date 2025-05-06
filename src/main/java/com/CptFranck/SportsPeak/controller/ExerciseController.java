package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.ExerciseInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class ExerciseController {

    private final ExerciseService exerciseService;

    private final ExerciseInputResolver exerciseInputResolver;

    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseController(ExerciseService exerciseService, ExerciseInputResolver exerciseInputResolver,
                              Mapper<ExerciseEntity, ExerciseDto> exerciseMapper) {
        this.exerciseMapper = exerciseMapper;
        this.exerciseService = exerciseService;
        this.exerciseInputResolver = exerciseInputResolver;
    }

    @DgsQuery
    public List<ExerciseDto> getExercises() {
        return exerciseService.findAll().stream().map(exerciseMapper::mapTo).toList();
    }

    @DgsQuery
    public ExerciseDto getExerciseById(@InputArgument Long id) {
        return exerciseMapper.mapTo(exerciseService.findOne(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public ExerciseDto addExercise(@InputArgument InputNewExercise inputNewExercise) {
        ExerciseEntity exercise = exerciseInputResolver.resolveInput(inputNewExercise);
        return exerciseMapper.mapTo(exerciseService.save(exercise));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public ExerciseDto modifyExercise(@InputArgument InputExercise inputExercise) {
        ExerciseEntity exercise = exerciseInputResolver.resolveInput(inputExercise);
        return exerciseMapper.mapTo(exerciseService.save(exercise));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public Long deleteExercise(@InputArgument Long exerciseId) {
        exerciseService.delete(exerciseId);
        return exerciseId;
    }
}
