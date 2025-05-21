package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuscleRepository extends CrudRepository<MuscleEntity, Long> {
}
