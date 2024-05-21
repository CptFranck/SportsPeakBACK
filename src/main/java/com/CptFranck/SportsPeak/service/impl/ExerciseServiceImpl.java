package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public ExerciseEntity save(ExerciseEntity exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public List<ExerciseEntity> findAll() {
        return StreamSupport.stream(exerciseRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ExerciseEntity> findOne(Long id) {
        return exerciseRepository.findById(id);
    }

    @Override
    public Set<ExerciseEntity> findMany(Set<Long> ids) {
        return ids.stream()
                .map(this::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateExerciseTypeRelation(Set<Long> newIds, Set<Long> oldIds, ExerciseTypeEntity exerciseType) {

        this.findMany(oldIds).forEach(e -> {
            e.getExerciseTypes().removeIf(et -> Objects.equals(et.getId(), exerciseType.getId()));
            this.save(e);
        });

        this.findMany(newIds).forEach(e -> {
            e.getExerciseTypes().add(exerciseType);
            this.save(e);
        });
    }

    @Override
    public void updateMuscleRelation(Set<Long> newIds, Set<Long> oldIds, MuscleEntity muscle) {
        this.findMany(oldIds).forEach(e -> {
            e.getMuscles().removeIf(et -> Objects.equals(et.getId(), muscle.getId()));
            this.save(e);
        });

        this.findMany(newIds).forEach(e -> {
            e.getMuscles().add(muscle);
            this.save(e);
        });
    }

    @Override
    public boolean exists(Long id) {
        return exerciseRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        exerciseRepository.deleteById(id);
    }
}
