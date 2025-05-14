package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<ProgExerciseEntity> findAll() {
        return StreamSupport.stream(progExerciseRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public ProgExerciseEntity findOne(Long id) {
        return progExerciseRepository.findById(id).orElseThrow(() -> new ProgExerciseNotFoundException(id));
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
    public ProgExerciseEntity save(ProgExerciseEntity progExercise) {
        if (progExercise.getId() == null)
            return progExerciseRepository.save(progExercise);
        if (exists(progExercise.getId()))
            return progExerciseRepository.save(progExercise);
        throw new ProgExerciseNotFoundException(progExercise.getId());
    }

    @Override
    public boolean exists(Long id) {
        return progExerciseRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        ProgExerciseEntity progExercise = this.findOne(id);
        progExerciseRepository.delete(progExercise);
    }
}
