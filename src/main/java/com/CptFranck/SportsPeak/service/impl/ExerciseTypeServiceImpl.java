package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExerciseTypeServiceImpl implements ExerciseTypeService {

    private final ExerciseService exerciseService;

    private final ExerciseTypeRepository exerciseTypeRepository;


    public ExerciseTypeServiceImpl(ExerciseService exerciseService, ExerciseTypeRepository exerciseTypeRepository) {
        this.exerciseService = exerciseService;
        this.exerciseTypeRepository = exerciseTypeRepository;
    }

    @Override
    public List<ExerciseTypeEntity> findAll() {
        return StreamSupport.stream(exerciseTypeRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseTypeEntity findOne(Long id) {
        return exerciseTypeRepository.findById(id).orElseThrow(() -> new ExerciseTypeNotFoundException(id));
    }

    @Override
    public Set<ExerciseTypeEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(exerciseTypeRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public ExerciseTypeEntity save(ExerciseTypeEntity exerciseType) {
        Set<Long> oldExerciseType;
        if (exerciseType.getId() == null)
            oldExerciseType = Collections.emptySet();
        else
            oldExerciseType = this.findOne(exerciseType.getId()).getExercises()
                    .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        Set<Long> newExerciseType = exerciseType.getExercises()
                .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeRepository.save(exerciseType);

        exerciseService.updateExerciseTypeRelation(
                newExerciseType,
                oldExerciseType,
                exerciseTypeSaved);

        return exerciseTypeSaved;
    }

    @Override
    public boolean exists(Long id) {
        return exerciseTypeRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.findById(id).orElseThrow(() -> new ExerciseTypeNotFoundException(id));
        exerciseTypeRepository.delete(exerciseType);
    }
}
