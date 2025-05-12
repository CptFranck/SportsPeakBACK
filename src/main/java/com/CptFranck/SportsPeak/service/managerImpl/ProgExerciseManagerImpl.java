package com.CptFranck.SportsPeak.service.managerImpl;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.service.ProgExerciseManager;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Service;

@Service
public class ProgExerciseManagerImpl implements ProgExerciseManager {

    private final TargetSetService targetSetService;

    private final ProgExerciseService progExerciseService;

    public ProgExerciseManagerImpl(TargetSetService targetSetService, ProgExerciseService progExerciseService) {
        this.targetSetService = targetSetService;
        this.progExerciseService = progExerciseService;
    }

    @Override
    public TargetSetEntity saveTargetSet(TargetSetEntity targetSet, Long targetSetUpdateId) {
        TargetSetEntity targetSetSaved = targetSetService.save(targetSet);

        ProgExerciseEntity progExercise = targetSet.getProgExercise();
        progExercise.getTargetSets().add(targetSet);
        progExerciseService.save(progExercise);

        if (targetSetUpdateId != null)
            targetSetService.setTheUpdate(targetSetSaved, targetSetUpdateId);

        return targetSetSaved;
    }
}
