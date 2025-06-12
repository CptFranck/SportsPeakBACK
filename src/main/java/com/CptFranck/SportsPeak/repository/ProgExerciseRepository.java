package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgExerciseRepository extends JpaRepository<ProgExerciseEntity, Long> {
    List<ProgExerciseEntity> findAllBySubscribedUsersId(Long id);
}
