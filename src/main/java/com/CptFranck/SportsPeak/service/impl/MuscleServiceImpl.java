package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MuscleServiceImpl implements MuscleService {

    MuscleRepository muscleRepository;

    public MuscleServiceImpl(MuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }

    @Override
    public MuscleEntity save(MuscleEntity muscleEntity) {
        return muscleRepository.save(muscleEntity);
    }

    @Override
    public List<MuscleEntity> findAll() {
        return StreamSupport.stream(muscleRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MuscleEntity> findOne(Long id) {
        return muscleRepository.findById(id);
    }

    @Override
    public Set<MuscleEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(muscleRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean exists(Long id) {
        return muscleRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        MuscleEntity muscle = muscleRepository.findById(id).orElseThrow(() -> new MuscleNotFoundException(id));
        muscleRepository.delete(muscle);
    }
}
