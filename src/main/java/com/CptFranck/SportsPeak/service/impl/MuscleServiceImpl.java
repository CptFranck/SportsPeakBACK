package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public void delete(Long id) {
        muscleRepository.deleteById(id);
    }
}
