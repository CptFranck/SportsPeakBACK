package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        Optional<ExerciseTypeEntity> exerciseType = exerciseTypeService.findOne(id);
        return exerciseType.map(exerciseTypeMapper::mapTo).orElse(null);
    }

    @DgsMutation
    public ExerciseTypeDto addExerciseType(@InputArgument InputNewExerciseType inputNewExerciseType) {
        return exerciseTypeMapper.mapTo(exerciseTypeService.save(inputToEntity(inputNewExerciseType)));
    }

    @DgsMutation
    public ExerciseTypeDto modifyExerciseType(@InputArgument InputExerciseType inputExerciseType) {
        if (!exerciseTypeService.exists(inputExerciseType.getId())) {
            return null;
        }
        return exerciseTypeMapper.mapTo(exerciseTypeService.save(inputToEntity(inputExerciseType)));
    }

    @DgsMutation
    public Long deleteExerciseType(@InputArgument Long exerciseTypeId) {
        if (!exerciseTypeService.exists(exerciseTypeId)) {
            return null;
        }
        exerciseTypeService.delete(exerciseTypeId);
        return exerciseTypeId;
    }

    private ExerciseTypeEntity inputToEntity(InputNewExerciseType inputNewExerciseType) {
        Set<Long> oldExerciseIds = new HashSet<Long>();
        Set<Long> newExerciseIds = Sets.newHashSet(inputNewExerciseType.getExerciseIds());

        Long id = null;
        if (inputNewExerciseType instanceof InputExerciseType) {
            id = ((InputExerciseType) inputNewExerciseType).getId();
            Optional<ExerciseTypeEntity> exerciseType = exerciseTypeService.findOne(id);
            exerciseType.ifPresent(exerciseTypeEntity ->
                    exerciseTypeEntity.getExercises().forEach(e -> oldExerciseIds.add(e.getId())));
        }
        ExerciseTypeEntity exerciseType = new ExerciseTypeEntity(
                id,
                inputNewExerciseType.getName(),
                inputNewExerciseType.getGoal(),
                Set.of()
        );
        exerciseService.updateExerciseTypeRelation(newExerciseIds, oldExerciseIds, exerciseType);
        return exerciseType;
    }
}
