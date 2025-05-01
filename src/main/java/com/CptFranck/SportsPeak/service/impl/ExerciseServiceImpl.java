package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.resolvers.ExerciseInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final ExerciseInputResolver exerciseInputResolver;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseInputResolver exerciseInputResolver) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseInputResolver = exerciseInputResolver;
    }

    @Override
    public ExerciseEntity create(InputNewExercise inputNewExercise) {
        ExerciseEntity exercise = exerciseInputResolver.resolveInput(inputNewExercise);
        return exerciseRepository.save(exercise);
    }

    @Override
    public ExerciseEntity update(InputExercise inputExercise) {
        ExerciseEntity oldExercise = this.findOne(inputExercise.getId());
        ExerciseEntity updatedExercise = exerciseInputResolver.resolveInput(inputExercise, oldExercise);
        return exerciseRepository.save(updatedExercise);
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
    public ExerciseEntity findOne(Long id) {
        return exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    @Override
    public Set<ExerciseEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(exerciseRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateExerciseTypeRelation(Set<Long> newIds, Set<Long> oldIds, ExerciseTypeEntity exerciseType) {
        this.findMany(oldIds).forEach(e -> {
            e.getExerciseTypes().removeIf(et -> Objects.equals(et.getId(), exerciseType.getId()));
            exerciseRepository.save(e);
        });

        this.findMany(newIds).forEach(e -> {
            e.getExerciseTypes().add(exerciseType);
            exerciseRepository.save(e);
        });
    }

    @Override
    public void updateMuscleRelation(Set<Long> newIds, Set<Long> oldIds, MuscleEntity muscle) {
        this.findMany(oldIds).forEach(e -> {
            e.getMuscles().removeIf(et -> Objects.equals(et.getId(), muscle.getId()));
            exerciseRepository.save(e);
        });

        this.findMany(newIds).forEach(e -> {
            e.getMuscles().add(muscle);
            exerciseRepository.save(e);
        });
    }

    @Override
    public void updateProgExerciseRelation(ExerciseEntity newExercise, ExerciseEntity oldExercise, ProgExerciseEntity progExercise) {
        oldExercise.getProgExercises().removeIf(progEx -> progEx.getId().equals(progExercise.getId()));
        newExercise.getProgExercises().add(progExercise);
        exerciseRepository.save(oldExercise);
        exerciseRepository.save(newExercise);
    }

    @Override
    public boolean exists(Long id) {
        return exerciseRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        ExerciseEntity exercise = exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException(id));
        exerciseRepository.delete(exercise);
    }
}
