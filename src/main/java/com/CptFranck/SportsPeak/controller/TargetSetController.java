package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.TargetSetInputResolver;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class TargetSetController {

    private final TargetSetService targetSetService;

    private final TargetSetInputResolver targetSetInputResolver;

    private final Mapper<TargetSetEntity, TargetSetDto> targetSetMapper;

    public TargetSetController(TargetSetService targetSetService, Mapper<TargetSetEntity, TargetSetDto> targetSetMapper, PerformanceLogService performanceLogService, TargetSetInputResolver targetSetInputResolver) {
        this.targetSetMapper = targetSetMapper;
        this.targetSetService = targetSetService;
        this.targetSetInputResolver = targetSetInputResolver;
    }

    @DgsQuery
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<TargetSetDto> getTargetSets() {
        return targetSetService.findAll().stream().map(targetSetMapper::mapTo).toList();
    }

    @DgsQuery
    @PreAuthorize("hasRole('ROLE_USER')")
    public TargetSetDto getTargetSetById(@InputArgument Long id) {
        TargetSetEntity targetSet = targetSetService.findOne(id);
        return targetSetMapper.mapTo(targetSet);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsQuery
    public List<TargetSetDto> getTargetSetsByProgExerciseId(@InputArgument Long progExerciseId) {
        return targetSetService.findAllByProgExerciseId(progExerciseId).stream().map(targetSetMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public TargetSetDto addTargetSet(@InputArgument InputNewTargetSet inputNewTargetSet) {
        TargetSetEntity targetSet = targetSetInputResolver.resolveInput(inputNewTargetSet);
        TargetSetEntity targetSetSaved = targetSetService.save(targetSet);
        targetSetService.setTheUpdate(targetSetSaved, inputNewTargetSet.getTargetSetUpdateId());
        return targetSetMapper.mapTo(targetSetSaved);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public TargetSetDto modifyTargetSet(@InputArgument InputTargetSet inputTargetSet) {
        TargetSetEntity targetSet = targetSetInputResolver.resolveInput(inputTargetSet);
        return targetSetMapper.mapTo(targetSetService.save(targetSet));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public List<TargetSetDto> modifyTargetSetState(@InputArgument InputTargetSetState inputTargetSetState) {
        TargetSetState state = TargetSetState.valueOfLabel(inputTargetSetState.getState());
        TargetSetEntity targetSet = targetSetService.updateTargetStates(inputTargetSetState.getId(), state);
        return targetSet.getProgExercise().getTargetSets().stream().map(targetSetMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deleteTargetSet(@InputArgument Long targetSetId) {
        targetSetService.delete(targetSetId);
        return targetSetId;
    }
}


