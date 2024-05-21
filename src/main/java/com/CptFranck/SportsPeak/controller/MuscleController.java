package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.InputNewMuscle;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Sets;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class MuscleController {

    private final ExerciseService exerciseService;
    private final MuscleService muscleService;
    private final Mapper<MuscleEntity, MuscleDto> muscleMapper;

    public MuscleController(ExerciseService exerciseService, MuscleService muscleService, Mapper<MuscleEntity, MuscleDto> muscleMapper) {
        this.exerciseService = exerciseService;
        this.muscleService = muscleService;
        this.muscleMapper = muscleMapper;
    }

    @DgsQuery
    public List<MuscleDto> getMuscles() {
        return muscleService.findAll().stream().map(muscleMapper::mapTo).toList();
    }

    @DgsQuery
    public MuscleDto getMuscleById(@InputArgument Long id) {
        Optional<MuscleEntity> muscleEntity = muscleService.findOne(id);
        return muscleEntity.map(muscleMapper::mapTo).orElse(null);
    }

    @DgsMutation
    public MuscleDto addMuscle(@InputArgument InputNewMuscle inputNewMuscle) {
        return muscleMapper.mapTo(muscleService.save(inputToEntity(inputNewMuscle)));
    }

    @DgsMutation
    public MuscleDto modifyMuscle(@InputArgument InputMuscle inputMuscle) {
        if (!muscleService.exists(inputMuscle.getId())) {
            return null;
        }
        return muscleMapper.mapTo(muscleService.save(inputToEntity(inputMuscle)));
    }

    @DgsMutation
    public Long deleteMuscle(@InputArgument Long muscleId) {
        if (!muscleService.exists(muscleId)) {
            return null;
        }
        muscleService.delete(muscleId);
        return muscleId;
    }

    private MuscleEntity inputToEntity(InputNewMuscle inputNewMuscle) {
        Set<Long> exerciseIds = Sets.newHashSet(inputNewMuscle.getExerciseIds());

        Set<ExerciseEntity> exercises = exerciseService.findMany(exerciseIds);

        Long id = null;
        if (inputNewMuscle instanceof InputMuscle) {
            id = ((InputMuscle) inputNewMuscle).getId();
        }
        return new MuscleEntity(
                id,
                inputNewMuscle.getName(),
                inputNewMuscle.getDescription(),
                inputNewMuscle.getFunction(),
                exercises
        );
    }
}
