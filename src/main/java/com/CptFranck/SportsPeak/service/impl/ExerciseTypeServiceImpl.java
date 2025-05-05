package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputExerciseType;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.resolvers.ExerciseTypeInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExerciseTypeServiceImpl implements ExerciseTypeService {

    private final ExerciseService exerciseService;

    private final ExerciseTypeRepository exerciseTypeRepository;

    private final ExerciseTypeInputResolver exerciseTypeInputResolver;

    public ExerciseTypeServiceImpl(ExerciseService exerciseService, ExerciseTypeRepository exerciseTypeRepository, ExerciseTypeInputResolver exerciseTypeInputResolver) {
        this.exerciseService = exerciseService;
        this.exerciseTypeRepository = exerciseTypeRepository;
        this.exerciseTypeInputResolver = exerciseTypeInputResolver;
    }

    @Override
    public ExerciseTypeEntity create(InputNewExerciseType inputNewExercise) {
        ExerciseTypeEntity exerciseType = exerciseTypeInputResolver.resolveInput(inputNewExercise);
        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeRepository.save(exerciseType);

        exerciseService.updateExerciseTypeRelation(
                new HashSet<>(inputNewExercise.getExerciseIds()),
                Collections.emptySet(),
                exerciseTypeSaved);

        return exerciseTypeSaved;
    }

    @Override
    public ExerciseTypeEntity update(InputExerciseType inputExercise) {
        ExerciseTypeEntity oldExerciseType = this.findOne(inputExercise.getId());
        ExerciseTypeEntity updatedExerciseType = exerciseTypeInputResolver.resolveInput(inputExercise, oldExerciseType);
        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeRepository.save(updatedExerciseType);

        exerciseService.updateExerciseTypeRelation(
                new HashSet<>(inputExercise.getExerciseIds()),
                oldExerciseType.getExercises().stream().map(ExerciseEntity::getId).collect(Collectors.toSet()),
                exerciseTypeSaved);

        return exerciseTypeSaved;
    }

    @Override
    public ExerciseTypeEntity save(ExerciseTypeEntity exerciseTypeEntity) {
        return exerciseTypeRepository.save(exerciseTypeEntity);
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
    public boolean exists(Long id) {
        return exerciseTypeRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        ExerciseTypeEntity muscle = exerciseTypeRepository.findById(id).orElseThrow(() -> new ExerciseTypeNotFoundException(id));
        exerciseTypeRepository.delete(muscle);
    }
}
