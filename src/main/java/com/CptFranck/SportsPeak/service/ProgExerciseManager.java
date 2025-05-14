package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;

public interface ProgExerciseManager {

    TargetSetEntity saveTargetSet(TargetSetEntity targetSet, Long targetSetUpdateId);

    void deleteProgExercise(Long id);

}
