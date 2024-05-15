package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;

import java.util.List;
import java.util.Optional;

public interface MuscleService {

    List<MuscleEntity> findAll();

    Optional<MuscleEntity> findOne(Long id);

    MuscleEntity save(MuscleEntity muscleEntity);

    void delete(Long id);
}