package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;

import java.util.List;
import java.util.Set;

public interface TargetSetService {

    List<TargetSetEntity> findAll();

    TargetSetEntity findOne(Long id);

    List<TargetSetEntity> findAllByProgExerciseId(Long progExerciseId);

    Set<TargetSetEntity> findMany(Set<Long> ids);

    boolean exists(Long id);

    TargetSetEntity save(TargetSetEntity targetExerciseSet);

    void delete(Long id);

    void updatePreviousUpdateState(Long id, TargetSetState state);
}