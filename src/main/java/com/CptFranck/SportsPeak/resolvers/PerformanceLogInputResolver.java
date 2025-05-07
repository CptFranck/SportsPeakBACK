package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Component;

@Component
public class PerformanceLogInputResolver {

    private final TargetSetService targetSetService;

    public PerformanceLogInputResolver(TargetSetService targetSetService) {
        this.targetSetService = targetSetService;
    }

    public PerformanceLogEntity resolveInput(InputNewPerformanceLog inputNewPerformanceLog) {
        TargetSetEntity targetSet = targetSetService.findOne(inputNewPerformanceLog.getTargetSetId());

        Long id;
        if (inputNewPerformanceLog instanceof InputPerformanceLog) {
            id = ((InputPerformanceLog) inputNewPerformanceLog).getId();
        } else {
            id = null;
        }

        return new PerformanceLogEntity(
                id,
                inputNewPerformanceLog.getSetIndex(),
                inputNewPerformanceLog.getRepetitionNumber(),
                inputNewPerformanceLog.getWeight(),
                WeightUnit.valueOfLabel(inputNewPerformanceLog.getWeightUnit()),
                inputNewPerformanceLog.getLogDate(),
                targetSet
        );
    }
}
