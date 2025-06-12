package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TargetSetRepository extends JpaRepository<TargetSetEntity, Long> {
    List<TargetSetEntity> findAllByProgExerciseId(Long progExerciseId);

    Optional<TargetSetEntity> findByTargetSetUpdateId(Long targetSetUpdateId);
}
