package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Sets;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DgsComponent
public class ExerciseTypeController {

    private final ExerciseService exerciseService;
    private final ExerciseTypeService exerciseTypeService;
    private final Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    public ExerciseTypeController(ExerciseService exerciseService, ExerciseTypeService exerciseTypeService, Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseMapper) {
        this.exerciseService = exerciseService;
        this.exerciseTypeService = exerciseTypeService;
        this.exerciseTypeMapper = exerciseMapper;
    }

    @DgsQuery
    public List<ExerciseTypeDto> getExerciseTypes() {
        return exerciseTypeService.findAll().stream().map(exerciseTypeMapper::mapTo).toList();
    }

    @DgsQuery
    public ExerciseTypeDto getExerciseTypeById(@InputArgument Long id) {
        ExerciseTypeEntity exerciseType = exerciseTypeService.findOne(id)
                .orElseThrow(() -> new ExerciseTypeNotFoundException(id));
        return exerciseTypeMapper.mapTo(exerciseType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public ExerciseTypeDto addExerciseType(@InputArgument InputNewExerciseType inputNewExerciseType) {
        return exerciseTypeMapper.mapTo(inputToEntity(inputNewExerciseType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public ExerciseTypeDto modifyExerciseType(@InputArgument InputExerciseType inputExerciseType) {
        return exerciseTypeMapper.mapTo(inputToEntity(inputExerciseType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public Long deleteExerciseType(@InputArgument Long exerciseTypeId) {
        exerciseTypeService.delete(exerciseTypeId);
        return exerciseTypeId;
    }

    private ExerciseTypeEntity inputToEntity(InputNewExerciseType inputNewExerciseType) {
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = Sets.newHashSet(inputNewExerciseType.getExerciseIds());

        Set<ExerciseEntity> exercises = exerciseService.findMany(newExerciseIds);

        Long id;
        if (inputNewExerciseType instanceof InputExerciseType) {
            id = ((InputExerciseType) inputNewExerciseType).getId();
            ExerciseTypeEntity exerciseType = exerciseTypeService.findOne(id)
                    .orElseThrow(() -> new ExerciseTypeNotFoundException(id));
            exerciseType.getExercises().forEach(e -> oldExerciseIds.add(e.getId()));
        } else {
            id = null;
        }
        ExerciseTypeEntity exerciseType = new ExerciseTypeEntity(
                id,
                inputNewExerciseType.getName(),
                inputNewExerciseType.getGoal(),
                exercises
        );
        exerciseType = exerciseTypeService.save(exerciseType);
        exerciseService.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType);
        return exerciseType;
    }
}
