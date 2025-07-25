package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repository.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExerciseTypeServiceImpl implements ExerciseTypeService {

    private final ExerciseTypeRepository exerciseTypeRepository;

    public ExerciseTypeServiceImpl(ExerciseTypeRepository exerciseTypeRepository) {
        this.exerciseTypeRepository = exerciseTypeRepository;
    }

    @Override
    public List<ExerciseTypeEntity> findAll() {
        return new ArrayList<>(exerciseTypeRepository.findAll());
    }

    @Override
    public ExerciseTypeEntity findOne(Long id) {
        return exerciseTypeRepository.findById(id).orElseThrow(() -> new ExerciseTypeNotFoundException(id));
    }

    @Override
    public Set<ExerciseTypeEntity> findMany(Set<Long> ids) {
        return new HashSet<>(exerciseTypeRepository.findAllById(ids));
    }

    @Override
    public ExerciseTypeEntity save(ExerciseTypeEntity exerciseType) {
        if (exerciseType.getId() == null)
            return exerciseTypeRepository.save(exerciseType);
        if (this.exists(exerciseType.getId()))
            return exerciseTypeRepository.save(exerciseType);
        throw new ExerciseTypeNotFoundException(exerciseType.getId());
    }

    @Override
    public void delete(Long id) {
        ExerciseTypeEntity exerciseType = this.findOne(id);
        if (exerciseType.getExercises().isEmpty())
            exerciseTypeRepository.delete(exerciseType);
        else
            throw new ExerciseTypeStillUsedInExerciseException(id, (long) exerciseType.getExercises().size());
    }

    @Override
    public boolean exists(Long id) {
        return exerciseTypeRepository.existsById(id);
    }
}
