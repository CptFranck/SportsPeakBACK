package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public PrivilegeEntity save(PrivilegeEntity privilege) {
        Optional<PrivilegeEntity> privilegeOptional = privilegeRepository.findByName(privilege.getName());
        if (privilegeOptional.isPresent() &&
                !Objects.equals(privilegeOptional.get().getId(), privilege.getId())) {
            throw new PrivilegeExistsException();
        }
        return privilegeRepository.save(privilege);
    }

    @Override
    public List<PrivilegeEntity> findAll() {
        return StreamSupport.stream(privilegeRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PrivilegeEntity> findOne(Long id) {
        return privilegeRepository.findById(id);
    }

    @Override
    public Set<PrivilegeEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(privilegeRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean exists(Long id) {
        return privilegeRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        PrivilegeEntity exercise = privilegeRepository.findById(id).orElseThrow(() -> new PrivilegeNotFoundException(id));
        privilegeRepository.delete(exercise);
    }
}
