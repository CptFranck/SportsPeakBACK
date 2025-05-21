package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolver.ExerciseTypeInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseManager;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class ExerciseTypeController {

    private final ExerciseManager exerciseManager;

    private final ExerciseTypeService exerciseTypeService;

    private final ExerciseTypeInputResolver exerciseTypeInputResolver;

    private final Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    public ExerciseTypeController(ExerciseManager exerciseManager, ExerciseTypeService exerciseTypeService, ExerciseTypeInputResolver exerciseTypeInputResolver, Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseMapper) {
        this.exerciseManager = exerciseManager;
        this.exerciseTypeMapper = exerciseMapper;
        this.exerciseTypeService = exerciseTypeService;
        this.exerciseTypeInputResolver = exerciseTypeInputResolver;
    }

    @DgsQuery
    public List<ExerciseTypeDto> getExerciseTypes() {
        return exerciseTypeService.findAll().stream().map(exerciseTypeMapper::mapTo).toList();
    }

    @DgsQuery
    public ExerciseTypeDto getExerciseTypeById(@InputArgument Long id) {
        return exerciseTypeMapper.mapTo(exerciseTypeService.findOne(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public ExerciseTypeDto addExerciseType(@InputArgument InputNewExerciseType inputNewExerciseType) {
        ExerciseTypeEntity exerciseType = exerciseTypeInputResolver.resolveInput(inputNewExerciseType);
        return exerciseTypeMapper.mapTo(exerciseManager.saveExerciseType(exerciseType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public ExerciseTypeDto modifyExerciseType(@InputArgument InputExerciseType inputExerciseType) {
        ExerciseTypeEntity exerciseType = exerciseTypeInputResolver.resolveInput(inputExerciseType);
        return exerciseTypeMapper.mapTo(exerciseManager.saveExerciseType(exerciseType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public Long deleteExerciseType(@InputArgument Long exerciseTypeId) {
        exerciseTypeService.delete(exerciseTypeId);
        return exerciseTypeId;
    }
}
