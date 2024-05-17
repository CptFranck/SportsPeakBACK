package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MuscleService {

    List<MuscleEntity> findAll();

    Optional<MuscleEntity> findOne(Long id);

    Set<MuscleEntity> findMany(Set<Long> muscleIds);

    boolean exists(Long id);

    MuscleEntity save(MuscleEntity muscleEntity);

    void delete(Long id);
}