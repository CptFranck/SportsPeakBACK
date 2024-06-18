package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DgsMutation
    public MuscleDto addMuscle(@InputArgument InputNewMuscle inputNewMuscle) {
        System.out.println(inputNewMuscle);
        return muscleMapper.mapTo(inputToEntity(inputNewMuscle));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DgsMutation
    public MuscleDto modifyMuscle(@InputArgument InputMuscle inputMuscle) {
        if (!muscleService.exists(inputMuscle.getId())) {
            return null;
        }
        return muscleMapper.mapTo(inputToEntity(inputMuscle));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DgsMutation
    public Long deleteMuscle(@InputArgument Long muscleId) {
        if (!muscleService.exists(muscleId)) {
            return null;
        }
        muscleService.delete(muscleId);
        return muscleId;
    }

    private MuscleEntity inputToEntity(InputNewMuscle inputNewMuscle) {
        Set<Long> oldExerciseIds = new HashSet<>();
        Set<Long> newExerciseIds = Sets.newHashSet(inputNewMuscle.getExerciseIds());

        Set<ExerciseEntity> exercises = exerciseService.findMany(newExerciseIds);

        Long id = null;
        if (inputNewMuscle instanceof InputMuscle) {
            id = ((InputMuscle) inputNewMuscle).getId();
            Optional<MuscleEntity> exerciseType = muscleService.findOne(id);
            exerciseType.ifPresent(exerciseTypeEntity ->
                    exerciseTypeEntity.getExercises().forEach(e -> oldExerciseIds.add(e.getId())));
        }
        MuscleEntity muscle = new MuscleEntity(
                id,
                inputNewMuscle.getName(),
                inputNewMuscle.getDescription(),
                inputNewMuscle.getFunction(),
                exercises
        );

        muscleService.save(muscle);
        exerciseService.updateMuscleRelation(newExerciseIds, oldExerciseIds, muscle);
        return muscle;
    }
}
