package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.resolver.TargetSetInputResolver;
import com.CptFranck.SportsPeak.service.ProgExerciseManager;
import com.CptFranck.SportsPeak.service.TargetSetManager;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class TargetSetController {

    private final TargetSetManager targetSetManager;

    private final TargetSetService targetSetService;

    private final ProgExerciseManager progExerciseManager;

    private final TargetSetInputResolver targetSetInputResolver;

    private final Mapper<TargetSetEntity, TargetSetDto> targetSetMapper;

    public TargetSetController(TargetSetService targetSetService, Mapper<TargetSetEntity, TargetSetDto> targetSetMapper, TargetSetManager targetSetManage, ProgExerciseManager progExerciseManager, TargetSetInputResolver targetSetInputResolver) {
        this.targetSetMapper = targetSetMapper;
        this.targetSetManager = targetSetManage;
        this.targetSetService = targetSetService;
        this.progExerciseManager = progExerciseManager;
        this.targetSetInputResolver = targetSetInputResolver;
    }

    @DgsQuery
    @PreAuthorize("hasRole('USER')")
    public List<TargetSetDto> getTargetSets() {
        return targetSetService.findAll().stream().map(targetSetMapper::mapTo).toList();
    }

    @DgsQuery
    @PreAuthorize("hasRole('USER')")
    public TargetSetDto getTargetSetById(@InputArgument Long id) {
        TargetSetEntity targetSet = targetSetService.findOne(id);
        return targetSetMapper.mapTo(targetSet);
    }

    @PreAuthorize("hasRole('USER')")
    @DgsQuery
    public List<TargetSetDto> getTargetSetsByProgExerciseId(@InputArgument Long progExerciseId) {
        return targetSetService.findAllByProgExerciseId(progExerciseId).stream().map(targetSetMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public TargetSetDto addTargetSet(@InputArgument InputNewTargetSet inputNewTargetSet) {
        TargetSetEntity targetSet = targetSetInputResolver.resolveInput(inputNewTargetSet);
        return targetSetMapper.mapTo(progExerciseManager.saveTargetSet(targetSet, inputNewTargetSet.getTargetSetUpdateId()));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public TargetSetDto modifyTargetSet(@InputArgument InputTargetSet inputTargetSet) {
        TargetSetEntity targetSet = targetSetInputResolver.resolveInput(inputTargetSet);
        return targetSetMapper.mapTo(progExerciseManager.saveTargetSet(targetSet, null));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public List<TargetSetDto> modifyTargetSetState(@InputArgument InputTargetSetState inputTargetSetState) {
        TargetSetEntity targetSet = targetSetService.updateTargetStates(inputTargetSetState.getId(), targetSetInputResolver.resolveInput(inputTargetSetState));
        return targetSet.getProgExercise().getTargetSets().stream().map(targetSetMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public Long deleteTargetSet(@InputArgument Long targetSetId) {
        targetSetManager.deleteTargetSet(targetSetId);
        return targetSetId;
    }
}


