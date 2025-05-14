package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.targetSet.AbstractTargetSetInput;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class TargetSetInputResolver {

    private final TargetSetService targetSetService;

    private final ProgExerciseService progExerciseService;

    public TargetSetInputResolver(TargetSetService targetSetService, ProgExerciseService progExerciseService) {
        this.targetSetService = targetSetService;
        this.progExerciseService = progExerciseService;
    }

    public TargetSetEntity resolveInput(AbstractTargetSetInput targetSetInput) {
        Long id;
        LocalDateTime creationDate;
        ProgExerciseEntity progExercise;
        TargetSetEntity targetSetUpdate;
        TargetSetState targetSetState;
        Set<PerformanceLogEntity> performanceLogs = new HashSet<>();

        if (targetSetInput instanceof InputTargetSet inputTargetSet) {
            id = inputTargetSet.getId();
            TargetSetEntity targetSet = targetSetService.findOne(id);
            creationDate = targetSet.getCreationDate();
            progExercise = targetSet.getProgExercise();
            targetSetState = targetSet.getState();
            targetSetUpdate = targetSet.getTargetSetUpdate();
            performanceLogs.addAll(targetSet.getPerformanceLogs());
        } else {
            InputNewTargetSet inputNewTargetSet = ((InputNewTargetSet) targetSetInput);
            id = null;
            creationDate = inputNewTargetSet.getCreationDate();
            progExercise = progExerciseService.findOne(inputNewTargetSet.getProgExerciseId());
            targetSetUpdate = null;
            targetSetState = TargetSetState.USED;
        }

        return new TargetSetEntity(
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
    }

    public TargetSetState resolveInput(InputTargetSetState inputTargetSetState) {
        return TargetSetState.valueOfLabel(inputTargetSetState.getState());
    }
}
