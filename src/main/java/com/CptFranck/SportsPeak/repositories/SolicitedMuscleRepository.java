package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.compositeKey.SolicitedMuscleKey;
import org.springframework.data.repository.CrudRepository;

public interface SolicitedMuscleRepository extends CrudRepository<MuscleEntity, SolicitedMuscleKey> {
}
