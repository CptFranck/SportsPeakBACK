package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.repository.TargetSetRepository;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TargetSetServiceImpl implements TargetSetService {

    private final TargetSetRepository targetSetRepository;

    public TargetSetServiceImpl(TargetSetRepository targetSetRepository) {
        this.targetSetRepository = targetSetRepository;
    }

    @Override
    public List<TargetSetEntity> findAll() {
        return new ArrayList<>(targetSetRepository.findAll());
    }

    @Override
    public TargetSetEntity findOne(Long id) {
        return targetSetRepository.findById(id).orElseThrow(() -> new TargetSetNotFoundException(id));
    }

    @Override
    public List<TargetSetEntity> findAllByProgExerciseId(Long progExerciseId) {
        return targetSetRepository.findAllByProgExerciseId(progExerciseId);
    }

    @Override
    public Set<TargetSetEntity> findMany(Set<Long> ids) {
        return new HashSet<>(targetSetRepository.findAllById(ids));
    }

    @Override
    public TargetSetEntity save(TargetSetEntity targetSet) {
        if (targetSet.getId() == null)
            return targetSetRepository.save(targetSet);
        if (exists(targetSet.getId()))
            return targetSetRepository.save(targetSet);
        throw new TargetSetNotFoundException(targetSet.getId());
    }

    @Override
    public void delete(Long id) {
        TargetSetEntity currentTargetSet = this.findOne(id);
        Optional<TargetSetEntity> targetSetUpdated = targetSetRepository.findByTargetSetUpdateId(currentTargetSet.getId());
        targetSetUpdated.ifPresent(targetSetEntity -> {
            TargetSetEntity targetSetUpdatedBuCurrentTargetSet = currentTargetSet.getTargetSetUpdate();
            currentTargetSet.setTargetSetUpdate(null);
            targetSetRepository.save(currentTargetSet);
            targetSetEntity.setTargetSetUpdate(targetSetUpdatedBuCurrentTargetSet);
            targetSetRepository.save(targetSetEntity);
        });
        targetSetRepository.delete(currentTargetSet);
    }

    @Override
    public TargetSetEntity updateTargetStates(Long id, TargetSetState state) {
        TargetSetEntity targetSet = this.findOne(id);
        targetSet.setState(state);
        TargetSetEntity targetSetSaved = targetSetRepository.save(targetSet);

        Optional<TargetSetEntity> optionalTargetSet = targetSetRepository.findByTargetSetUpdateId(targetSetSaved.getId());
        optionalTargetSet.ifPresent(oldTargetSet -> this.updateTargetStates(oldTargetSet.getId(), state));
        return targetSetSaved;
    }

    @Override
    public boolean exists(Long id) {
        return targetSetRepository.existsById(id);
    }
}
