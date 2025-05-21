package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
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

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
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
    public ExerciseEntity save(ExerciseEntity exercise) {
        if (exercise.getId() == null)
            return exerciseRepository.save(exercise);
        if (exists(exercise.getId()))
            return exerciseRepository.save(exercise);
        throw new ExerciseNotFoundException(exercise.getId());
    }

    @Override
    public void delete(Long id) {
        ExerciseEntity exercise = this.findOne(id);
        exerciseRepository.delete(exercise);
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
    public boolean exists(Long id) {
        return exerciseRepository.existsById(id);
    }
}
