package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
}
