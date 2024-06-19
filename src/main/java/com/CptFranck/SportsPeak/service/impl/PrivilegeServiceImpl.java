package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public PrivilegeEntity save(PrivilegeEntity role) {
        return privilegeRepository.save(role);
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
        return ids.stream()
                .map(this::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean exists(Long id) {
        return privilegeRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        privilegeRepository.deleteById(id);
    }
}
