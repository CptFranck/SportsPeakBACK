package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.List;
import java.util.Set;

public interface MuscleService {

    List<MuscleEntity> findAll();

    MuscleEntity findOne(Long id);

    Set<MuscleEntity> findMany(Set<Long> ids);

    MuscleEntity save(MuscleEntity muscleEntity);

    void delete(Long id);

    boolean exists(Long id);
}