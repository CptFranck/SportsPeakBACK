package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProgExerciseServiceImpl implements ProgExerciseService {

    ProgExerciseRepository progExerciseRepository;

    public ProgExerciseServiceImpl(ProgExerciseRepository progExerciseRepository) {
        this.progExerciseRepository = progExerciseRepository;
    }

    @Override
    public ProgExerciseEntity save(ProgExerciseEntity progExercise) {
        return progExerciseRepository.save(progExercise);
    }

    @Override
    public List<ProgExerciseEntity> findAll() {
        return StreamSupport.stream(progExerciseRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProgExerciseEntity> findOne(Long id) {
        return progExerciseRepository.findById(id);
    }

    @Override
    public Set<ProgExerciseEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(progExerciseRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean exists(Long id) {
        return progExerciseRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        ProgExerciseEntity progExercise = progExerciseRepository.findById(id)
                .orElseThrow(() -> new ProgExerciseNotFoundException(id));
        progExerciseRepository.delete(progExercise);
    }
}
