package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
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
        MuscleEntity muscleEntity = muscleService.findOne(id).orElseThrow(() -> new MuscleNotFoundException(id));
        return muscleMapper.mapTo(muscleEntity);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public MuscleDto addMuscle(@InputArgument InputNewMuscle inputNewMuscle) {
        return muscleMapper.mapTo(inputToEntity(inputNewMuscle));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public MuscleDto modifyMuscle(@InputArgument InputMuscle inputMuscle) {
        return muscleMapper.mapTo(inputToEntity(inputMuscle));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public Long deleteMuscle(@InputArgument Long muscleId) {
        muscleService.delete(muscleId);
        return muscleId;
    }

    private MuscleEntity inputToEntity(InputNewMuscle inputNewMuscle) {
        MuscleEntity muscle = null;
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = Sets.newHashSet(inputNewMuscle.getExerciseIds());
        Set<ExerciseEntity> exercises = exerciseService.findMany(newExerciseIds);

        if (inputNewMuscle instanceof InputMuscle) {
            muscle = muscleService.findOne(((InputMuscle) inputNewMuscle).getId())
                    .orElseThrow(() -> new MuscleNotFoundException(((InputMuscle) inputNewMuscle).getId()));
            muscle.getExercises().forEach(e -> oldExerciseIds.add(e.getId()));
        } else {
            muscle = new MuscleEntity(
                    null,
                    inputNewMuscle.getName(),
                    inputNewMuscle.getDescription(),
                    inputNewMuscle.getFunction(),
                    exercises
            );
        }

        muscle = muscleService.save(muscle);
        exerciseService.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle);
        return muscle;
    }
}
