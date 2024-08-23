package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExerciseTypeServiceImpl implements ExerciseTypeService {

    ExerciseTypeRepository exerciseTypeRepository;

    public ExerciseTypeServiceImpl(ExerciseTypeRepository exerciseTypeRepository) {
        this.exerciseTypeRepository = exerciseTypeRepository;
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
    public Optional<ExerciseTypeEntity> findOne(Long id) {
        return exerciseTypeRepository.findById(id);
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
