package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MuscleServiceImpl implements MuscleService {

    private final ExerciseService exerciseService;

    private final MuscleRepository muscleRepository;

    public MuscleServiceImpl(ExerciseService exerciseService, MuscleRepository muscleRepository) {
        this.exerciseService = exerciseService;
        this.muscleRepository = muscleRepository;
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
    public MuscleEntity findOne(Long id) {
        return muscleRepository.findById(id).orElseThrow(() -> new MuscleNotFoundException(id));
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
    public MuscleEntity save(MuscleEntity muscle) {
        Set<Long> oldExerciseIds;
        if (muscle.getId() == null)
            oldExerciseIds = Collections.emptySet();
        else
            oldExerciseIds = this.findOne(muscle.getId()).getExercises()
                    .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());
        Set<Long> newExerciseIds = muscle.getExercises()
                .stream().map(ExerciseEntity::getId).collect(Collectors.toSet());

        MuscleEntity muscleSaved = muscleRepository.save(muscle);

        exerciseService.updateMuscleRelation(newExerciseIds,
                oldExerciseIds,
                muscleSaved);

        return muscleSaved;
    }

    @Override
    public boolean exists(Long id) {
        return muscleRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        MuscleEntity muscle = this.findOne(id);
        if (muscle.getExercises().isEmpty())
            muscleRepository.delete(muscle);
        else
            throw new MuscleStillUsedInExerciseException(id, (long) muscle.getExercises().size());
    }
}
