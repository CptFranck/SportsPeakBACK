package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Sets;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class
ExerciseController {

    private final MuscleService muscleService;
    private final ExerciseService exerciseService;
    private final ExerciseTypeService exerciseTypeService;
    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseController(ExerciseService exerciseService,
                              ExerciseTypeService exerciseTypeService,
                              MuscleService muscleService,
                              Mapper<ExerciseEntity, ExerciseDto> exerciseMapper) {
        this.muscleService = muscleService;
        this.exerciseService = exerciseService;
        this.exerciseTypeService = exerciseTypeService;
        this.exerciseMapper = exerciseMapper;
    }
    @DgsQuery
    public List<ExerciseDto> getExercises() {
        return exerciseService.findAll().stream().map(exerciseMapper::mapTo).toList();
    }

    @DgsQuery
    public ExerciseDto getExerciseById(@InputArgument Long id) {
        Optional<ExerciseEntity> exerciseEntity = exerciseService.findOne(id);
        return exerciseEntity.map(exerciseMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public ExerciseDto addExercise(@InputArgument InputNewExercise inputNewExercise) {
        return exerciseMapper.mapTo(inputToEntity(inputNewExercise));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public ExerciseDto modifyExercise(@InputArgument InputExercise inputExercise) {
        if (!exerciseService.exists(inputExercise.getId())) {
            return null;
        }
        return exerciseMapper.mapTo(inputToEntity(inputExercise));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public Long deleteExercise(@InputArgument Long exerciseId) {
        if (!exerciseService.exists(exerciseId)) {
            return null;
        }
        exerciseService.delete(exerciseId);
        return exerciseId;
    }

    private ExerciseEntity inputToEntity(InputNewExercise inputNewExercise) {
        Set<Long> muscleIds = Sets.newHashSet(inputNewExercise.getMuscleIds());
        Set<Long> exerciseTypeIds = Sets.newHashSet(inputNewExercise.getExerciseTypeIds());

        Set<MuscleEntity> muscles = muscleService.findMany(muscleIds);
        Set<ExerciseTypeEntity> exerciseTypes = exerciseTypeService.findMany(exerciseTypeIds);

        Long id;
        Set<ProgExerciseEntity> progExercises = new HashSet<>();
        if (inputNewExercise instanceof InputExercise) {
            id = ((InputExercise) inputNewExercise).getId();
            progExercises = exerciseService.findOne(id).orElseThrow(() -> new ExerciseNotFoundException(id))
                    .getProgExercises();
        } else {
            id = null;
        }
        ExerciseEntity exerciseEntity = new ExerciseEntity(
                id,
                inputNewExercise.getName(),
                inputNewExercise.getDescription(),
                inputNewExercise.getGoal(),
                exerciseTypes,
                muscles,
                progExercises
        );

        return exerciseService.save(exerciseEntity);
    }
}
