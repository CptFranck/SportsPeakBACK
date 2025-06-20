package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repository.MuscleRepository;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MuscleServiceImpl implements MuscleService {

    private final MuscleRepository muscleRepository;

    public MuscleServiceImpl(MuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }

    @Override
    public List<MuscleEntity> findAll() {
        return new ArrayList<>(muscleRepository.findAll());
    }

    @Override
    public MuscleEntity findOne(Long id) {
        return muscleRepository.findById(id).orElseThrow(() -> new MuscleNotFoundException(id));
    }

    @Override
    public Set<MuscleEntity> findMany(Set<Long> ids) {
        return new HashSet<>(muscleRepository.findAllById(ids));
    }

    @Override
    public MuscleEntity save(MuscleEntity muscle) {
        if (muscle.getId() == null)
            return muscleRepository.save(muscle);
        if (this.exists(muscle.getId()))
            return muscleRepository.save(muscle);
        throw new MuscleNotFoundException(muscle.getId());
    }

    @Override
    public void delete(Long id) {
        MuscleEntity muscle = this.findOne(id);
        if (muscle.getExercises().isEmpty())
            muscleRepository.delete(muscle);
        else
            throw new MuscleStillUsedInExerciseException(id, (long) muscle.getExercises().size());
    }

    @Override
    public boolean exists(Long id) {
        return muscleRepository.existsById(id);
    }
}
