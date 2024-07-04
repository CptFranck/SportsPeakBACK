package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetExerciseSetEntity;
import com.CptFranck.SportsPeak.repositories.TargetExerciseSetRepository;
import com.CptFranck.SportsPeak.service.TargetExerciseSetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TargetExerciseSetServiceImpl implements TargetExerciseSetService {

    TargetExerciseSetRepository targetExerciseSetRepository;

    public TargetExerciseSetServiceImpl(TargetExerciseSetRepository targetExerciseSetRepository) {
        this.targetExerciseSetRepository = targetExerciseSetRepository;
    }

    @Override
    public TargetExerciseSetEntity save(TargetExerciseSetEntity targetExerciseSet) {
        return targetExerciseSetRepository.save(targetExerciseSet);
    }

    @Override
    public List<TargetExerciseSetEntity> findAll() {
        return StreamSupport.stream(targetExerciseSetRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TargetExerciseSetEntity> findOne(Long id) {
        return targetExerciseSetRepository.findById(id);
    }

    @Override
    public Set<TargetExerciseSetEntity> findMany(Set<Long> ids) {
        return ids.stream()
                .map(this::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void updatePerformanceLogRelation(Set<Long> newIds, Set<Long> oldIds, PerformanceLogEntity performanceLog) {
        this.findMany(oldIds).forEach(e -> {
            e.getPerformanceLogs().removeIf(et -> Objects.equals(et.getId(), performanceLog.getId()));
            this.save(e);
        });

        this.findMany(newIds).forEach(e -> {
            e.getPerformanceLogs().add(performanceLog);
            this.save(e);
        });
    }


    @Override
    public boolean exists(Long id) {
        return targetExerciseSetRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        targetExerciseSetRepository.deleteById(id);
    }
}
