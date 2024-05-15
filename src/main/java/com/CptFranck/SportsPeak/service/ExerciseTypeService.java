package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;

import java.util.List;
import java.util.Optional;

public interface ExerciseTypeService {

    List<ExerciseTypeEntity> findAll();

    Optional<ExerciseTypeEntity> findOne(Long id);

    ExerciseTypeEntity save(ExerciseTypeEntity exerciseTypeEntity);

    void delete(Long id);
}