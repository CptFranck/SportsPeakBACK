package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity save(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public List<RoleEntity> findAll() {
        return StreamSupport.stream(roleRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleEntity> findOne(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Set<RoleEntity> findMany(Set<Long> ids) {
        return ids.stream()
                .map(this::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void updatePrivilegeRelation(Set<Long> newIds, Set<Long> oldIds, PrivilegeEntity privilegeEntity) {
        this.findMany(oldIds).forEach(p -> {
            p.getPrivileges().removeIf(et -> Objects.equals(et.getId(), privilegeEntity.getId()));
            this.save(p);
        });

        this.findMany(newIds).forEach(p -> {
            p.getPrivileges().add(privilegeEntity);
            this.save(p);
        });
    }

    @Override
    public boolean exists(Long id) {
        return roleRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
