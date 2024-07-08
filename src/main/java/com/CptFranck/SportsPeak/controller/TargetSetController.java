package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

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
        if (!targetSetService.exists(inputTargetSet.getId())) {
            return null;
        }
        return targetSetMapper.mapTo(inputToEntity(inputTargetSet));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deleteTargetSet(@InputArgument Long targetSetId) {
        if (!targetSetService.exists(targetSetId)) {
            return null;
        }
        targetSetService.delete(targetSetId);
        return targetSetId;
    }

    private TargetSetEntity inputToEntity(InputNewTargetSet inputNewTargetSet) {
        ProgExerciseEntity progExercise = progExerciseService.findOne(inputNewTargetSet.getProgExerciseId())
                .orElseThrow(() -> new ExerciseNotFoundException(inputNewTargetSet.getProgExerciseId()));
        TargetSetEntity targetSetUpdate = targetSetService.findOne(inputNewTargetSet.getTargetSetUpdateId())
                .orElse(null);

        Long id = null;
        Set<PerformanceLogEntity> performanceLogs = new HashSet<>();
        if (inputNewTargetSet instanceof InputTargetSet) {
            id = ((InputTargetSet) inputNewTargetSet).getId();
            Optional<TargetSetEntity> targetSet = targetSetService.findOne(id);
            targetSet.ifPresent(localTargetSet -> {
                performanceLogs.addAll(localTargetSet.getPerformanceLogs());
            });
        }

        TargetSetEntity targetSet = new TargetSetEntity(
                id,
                inputNewTargetSet.getSetNumber(),
                inputNewTargetSet.getRepetitionNumber(),
                inputNewTargetSet.getWeight(),
                inputNewTargetSet.getWeightUnit(),
                inputNewTargetSet.getPhysicalExertionUnitTime(),
                inputNewTargetSet.getRestTime(),
                inputNewTargetSet.getCreationDate(),
                progExercise,
                targetSetUpdate,
                performanceLogs
        );

        targetSet = targetSetService.save(targetSet);
        if (id == null) {
            progExercise.getTargetSets().add(targetSet);
            progExerciseService.save(progExercise);
        }
        return targetSet;
    }
}
