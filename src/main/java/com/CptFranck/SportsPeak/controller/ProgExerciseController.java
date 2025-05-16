package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.ProgExerciseInputResolver;
import com.CptFranck.SportsPeak.service.ProgExerciseManager;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class ProgExerciseController {

    private final ProgExerciseService progExerciseService;
    private final ProgExerciseManager progExerciseManager;
    private final ProgExerciseInputResolver progExerciseInputResolver;
    private final Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    public ProgExerciseController(ProgExerciseService progExerciseService, ProgExerciseManager progExerciseManager, ProgExerciseInputResolver progExerciseInputResolver, Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper) {
        this.progExerciseManager = progExerciseManager;
        this.progExerciseMapper = progExerciseMapper;
        this.progExerciseService = progExerciseService;
        this.progExerciseInputResolver = progExerciseInputResolver;
    }

    @DgsQuery
    public List<ProgExerciseDto> getProgExercises() {
        return progExerciseService.findAll().stream().map(progExerciseMapper::mapTo).toList();
    }

    @DgsQuery
    public ProgExerciseDto getProgExerciseById(@InputArgument Long id) {
        ProgExerciseEntity progExercise = progExerciseService.findOne(id);
        return progExerciseMapper.mapTo(progExercise);
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public ProgExerciseDto addProgExercise(@InputArgument InputNewProgExercise inputNewProgExercise) {
        ProgExerciseEntity progExercise = progExerciseInputResolver.resolveInput(inputNewProgExercise);
        return progExerciseMapper.mapTo(progExerciseService.save(progExercise));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public ProgExerciseDto modifyProgExercise(@InputArgument InputProgExercise inputProgExercise) {
        ProgExerciseEntity progExercise = progExerciseInputResolver.resolveInput(inputProgExercise);
        return progExerciseMapper.mapTo(progExerciseService.save(progExercise));
    }

    @PreAuthorize("hasRole('STAFF')")
    @DgsMutation
    public ProgExerciseDto modifyProgExerciseTrustLabel(@InputArgument InputProgExerciseTrustLabel inputProgExerciseTrustLabel) {
        ProgExerciseEntity progExercise = progExerciseInputResolver.resolveInput(inputProgExerciseTrustLabel);
        return progExerciseMapper.mapTo(progExerciseService.save(progExercise));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public Long deleteProgExercise(@InputArgument Long progExerciseId) {
        progExerciseManager.deleteProgExercise(progExerciseId);
        return progExerciseId;
    }
}
