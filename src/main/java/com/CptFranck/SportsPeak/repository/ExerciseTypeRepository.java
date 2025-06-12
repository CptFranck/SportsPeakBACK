package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseTypeRepository extends JpaRepository<ExerciseTypeEntity, Long> {
}
