package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
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

    private final TargetSetRepository targetSetRepository;

    public TargetSetServiceImpl(TargetSetRepository targetSetRepository) {
        this.targetSetRepository = targetSetRepository;
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
    public TargetSetEntity findOne(Long id) {
        return targetSetRepository.findById(id).orElseThrow(() -> new TargetSetNotFoundException(id));
    }

    @Override
    public List<TargetSetEntity> findAllByProgExerciseId(Long progExerciseId) {
        return targetSetRepository.findAllByProgExerciseId(progExerciseId);
    }

    @Override
    public Set<TargetSetEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(targetSetRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
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
    public void setTheUpdate(TargetSetEntity targetSet, Long targetSetUpdatedId) {
        TargetSetEntity targetSetUpdated = this.findOne(targetSetUpdatedId);
        targetSetUpdated.setTargetSetUpdate(targetSet);
        targetSetRepository.save(targetSetUpdated);
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
