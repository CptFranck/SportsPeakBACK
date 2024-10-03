package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.targetSet.AbstractTargetSetInput;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class TargetSetController {

    private final TargetSetService targetSetService;
    private final ProgExerciseService progExerciseService;
    private final Mapper<TargetSetEntity, TargetSetDto> targetSetMapper;

    public TargetSetController(TargetSetService targetSetService, ProgExerciseService progExerciseService, Mapper<TargetSetEntity, TargetSetDto> targetSetMapper) {
        this.targetSetService = targetSetService;
        this.progExerciseService = progExerciseService;
        this.targetSetMapper = targetSetMapper;
    }

    @DgsQuery
    public List<TargetSetDto> getTargetSets() {
        return targetSetService.findAll().stream().map(targetSetMapper::mapTo).toList();
    }

    @DgsQuery
    public TargetSetDto getTargetSetById(@InputArgument Long id) {
        Optional<TargetSetEntity> targetSet = targetSetService.findOne(id);
        return targetSet.map(targetSetMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsQuery
    public List<TargetSetDto> getTargetSetsByProgExerciseId(@InputArgument Long progExerciseId) {
        return targetSetService.findAllByProgExerciseId(progExerciseId).stream().map(targetSetMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public TargetSetDto addTargetSet(@InputArgument InputNewTargetSet inputNewTargetSet) {
        return targetSetMapper.mapTo(inputToEntity(inputNewTargetSet));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public TargetSetDto modifyTargetSet(@InputArgument InputTargetSet inputTargetSet) {
        return targetSetMapper.mapTo(inputToEntity(inputTargetSet));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public List<TargetSetDto> modifyTargetSetState(@InputArgument InputTargetSetState inputTargetSetState) {
        TargetSetEntity targetSet = inputTrustLabelToEntity(inputTargetSetState);
        return targetSet.getProgExercise().getTargetSets().stream().map(targetSetMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deleteTargetSet(@InputArgument Long targetSetId) {
        targetSetService.delete(targetSetId);
        return targetSetId;
    }

    private TargetSetEntity inputToEntity(AbstractTargetSetInput targetSetInput) {
        Long id;
        LocalDateTime creationDate = null;
        ProgExerciseEntity progExercise = null;
        TargetSetEntity targetSetUpdate = null;
        TargetSetEntity targetSetUpdated = null;
        TargetSetState targetSetState = TargetSetState.USED;
        Set<PerformanceLogEntity> performanceLogs = new HashSet<>();

        if (targetSetInput instanceof InputTargetSet inputTargetSet) {
            id = inputTargetSet.getId();
            TargetSetEntity targetSet = targetSetService.findOne(id).orElseThrow(
                    () -> new TargetSetNotFoundException(id));
            creationDate = targetSet.getCreationDate();
            progExercise = targetSet.getProgExercise();
            targetSetState = targetSet.getState();
            targetSetUpdate = targetSet.getTargetSetUpdate();
            performanceLogs.addAll(targetSet.getPerformanceLogs());
        } else {
            id = null;
            if (targetSetInput instanceof InputNewTargetSet inputNewTargetSet) {
                creationDate = inputNewTargetSet.getCreationDate();
                progExercise = progExerciseService.findOne(inputNewTargetSet.getProgExerciseId()).orElseThrow(
                        () -> new ProgExerciseNotFoundException(inputNewTargetSet.getProgExerciseId()));
                if (inputNewTargetSet.getTargetSetUpdateId() != null) {
                    targetSetUpdated = targetSetService.findOne(inputNewTargetSet.getTargetSetUpdateId()).orElseThrow(
                            () -> new TargetSetNotFoundException(inputNewTargetSet.getTargetSetUpdateId()));
                }
            }
        }

        TargetSetEntity targetSet = new TargetSetEntity(
                id,
                targetSetInput.getIndex(),
                targetSetInput.getSetNumber(),
                targetSetInput.getRepetitionNumber(),
                targetSetInput.getWeight(),
                WeightUnit.valueOfLabel(targetSetInput.getWeightUnit()),
                targetSetInput.getPhysicalExertionUnitTime().InputDurationToDuration(),
                targetSetInput.getRestTime().InputDurationToDuration(),
                creationDate,
                targetSetState,
                progExercise,
                targetSetUpdate,
                performanceLogs
        );

        targetSet = targetSetService.save(targetSet);
        if (id == null && progExercise != null) {
            progExercise.getTargetSets().add(targetSet);
            progExerciseService.save(progExercise);
        }
        if (targetSetUpdated != null) {
            targetSetUpdated.setTargetSetUpdate(targetSet);
            targetSetService.save(targetSetUpdated);
        }
        return targetSet;
    }

    private TargetSetEntity inputTrustLabelToEntity(InputTargetSetState inputTargetSetState) {
        TargetSetEntity targetSet = targetSetService.findOne(inputTargetSetState.getId()).orElseThrow(
                () -> new TargetSetNotFoundException(inputTargetSetState.getId()));
        TargetSetState state = TargetSetState.valueOfLabel(inputTargetSetState.getState());
        targetSetService.updatePreviousUpdateState(targetSet.getId(), state);
        targetSet.setState(state);
        return targetSetService.save(targetSet);
    }
}


