package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolver.MuscleInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseManager;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class MuscleController {

    private final MuscleService muscleService;

    private final ExerciseManager exerciseManager;

    private final MuscleInputResolver muscleInputResolver;

    private final Mapper<MuscleEntity, MuscleDto> muscleMapper;

    public MuscleController(MuscleService muscleService, ExerciseManager exerciseManager, MuscleInputResolver muscleInputResolver, Mapper<MuscleEntity, MuscleDto> muscleMapper) {
        this.exerciseManager = exerciseManager;
        this.muscleMapper = muscleMapper;
        this.muscleService = muscleService;
        this.muscleInputResolver = muscleInputResolver;
    }

    @DgsQuery
    public List<MuscleDto> getMuscles() {
        return muscleService.findAll().stream().map(muscleMapper::mapTo).toList();
    }

    @DgsQuery
    public MuscleDto getMuscleById(@InputArgument Long id) {
        MuscleEntity muscleEntity = muscleService.findOne(id);
        return muscleMapper.mapTo(muscleEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public MuscleDto addMuscle(@InputArgument InputNewMuscle inputNewMuscle) {
        MuscleEntity muscle = muscleInputResolver.resolveInput(inputNewMuscle);
        return muscleMapper.mapTo(exerciseManager.saveMuscle(muscle));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public MuscleDto modifyMuscle(@InputArgument InputMuscle inputMuscle) {
        MuscleEntity muscle = muscleInputResolver.resolveInput(inputMuscle);
        return muscleMapper.mapTo(exerciseManager.saveMuscle(muscle));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public Long deleteMuscle(@InputArgument Long muscleId) {
        muscleService.delete(muscleId);
        return muscleId;
    }
}
