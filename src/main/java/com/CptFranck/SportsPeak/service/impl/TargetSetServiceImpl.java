package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TargetSetServiceImpl implements TargetSetService {

    TargetSetRepository targetSetRepository;

    public TargetSetServiceImpl(TargetSetRepository targetExerciseSetRepository) {
        this.targetSetRepository = targetExerciseSetRepository;
    }

    @Override
    public TargetSetEntity save(TargetSetEntity targetExerciseSet) {
        return targetSetRepository.save(targetExerciseSet);
    }

    @Override
    public List<TargetSetEntity> findAll() {
        return StreamSupport.stream(targetSetRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TargetSetEntity> findOne(Long id) {
        return targetSetRepository.findById(id);
    }

    @Override
    public List<TargetSetEntity> findAllByProgExerciseId(Long progExerciseId) {
        return targetSetRepository.findAllByProgExerciseId(progExerciseId);
    }

    @Override
    public Set<TargetSetEntity> findMany(Set<Long> ids) {
        return ids.stream()
                .map(this::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean exists(Long id) {
        return targetSetRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        targetSetRepository.deleteById(id);
    }
}
