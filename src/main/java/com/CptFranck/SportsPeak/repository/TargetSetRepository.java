package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TargetSetRepository extends CrudRepository<TargetSetEntity, Long> {
    List<TargetSetEntity> findAllByProgExerciseId(Long progExerciseId);

    Optional<TargetSetEntity> findByTargetSetUpdateId(Long targetSetUpdateId);
}
