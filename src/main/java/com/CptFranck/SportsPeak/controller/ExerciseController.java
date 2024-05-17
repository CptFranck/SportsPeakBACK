package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.InputExercise;
import com.CptFranck.SportsPeak.domain.input.InputNewExercise;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@DgsComponent
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseTypeService exerciseTypeService;
    private final MuscleService muscleService;
    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseController(ExerciseService exerciseService, ExerciseTypeService exerciseTypeService, MuscleService muscleService, Mapper<ExerciseEntity, ExerciseDto> exerciseMapper) {
        this.exerciseService = exerciseService;
        this.exerciseTypeService = exerciseTypeService;
        this.muscleService = muscleService;
        this.exerciseMapper = exerciseMapper;
    }

    @DgsQuery
    public List<ExerciseDto> getExercises() {
        return exerciseService.findAll().stream().map(exerciseMapper::mapTo).toList();
    }

    @DgsQuery
    public ExerciseDto getExerciseById(@InputArgument Integer id) {
        Optional<ExerciseEntity> exerciseEntity = exerciseService.findOne(id.longValue());
        return exerciseEntity.map(exerciseMapper::mapTo).orElse(null);
    }

    @DgsMutation
    public ExerciseDto addExercise(@InputArgument InputNewExercise inputNewExercise) {
        return exerciseMapper.mapTo(exerciseService.save(inputToEntity(inputNewExercise)));
    }

    @DgsMutation
    public ExerciseDto modifyExercise(@InputArgument InputExercise inputExercise) {
        if (!exerciseService.exists(inputExercise.getId().longValue())) {
            return null;
        }
        return exerciseMapper.mapTo(exerciseService.save(inputToEntity(inputExercise)));
    }

    private ExerciseEntity inputToEntity(InputNewExercise inputExercise) {
        Set<Long> muscleIds = inputExercise.getExerciseTypeIds().stream()
                .map(Integer::longValue)
                .collect(Collectors.toSet());
        Set<Long> exerciseTypeIds = inputExercise.getExerciseTypeIds().stream()
                .map(Integer::longValue)
                .collect(Collectors.toSet());

        Set<MuscleEntity> muscles = muscleService.findMany(muscleIds);
        Set<ExerciseTypeEntity> exerciseTypes = exerciseTypeService.findMany(exerciseTypeIds);

        Long id = null;
        if (inputExercise instanceof InputExercise) {
            id = ((InputExercise) inputExercise).getId().longValue();
        }
        return new ExerciseEntity(
                id,
                inputExercise.getName(),
                inputExercise.getDescription(),
                inputExercise.getGoal(),
                exerciseTypes,
                muscles
        );
    }

    @DgsMutation
    public Integer deleteExercise(@InputArgument Integer exerciseId) {
        if (!exerciseService.exists(exerciseId.longValue())) {
            return null;
        }
        exerciseService.delete(exerciseId.longValue());
        return exerciseId;
    }
}
