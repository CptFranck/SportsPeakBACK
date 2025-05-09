package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    private final RoleService roleService;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, RoleService roleService) {
        this.privilegeRepository = privilegeRepository;
        this.roleService = roleService;
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
    public PrivilegeEntity findOne(Long id) {
        return privilegeRepository.findById(id).orElseThrow(() -> new PrivilegeNotFoundException(id));
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
    public PrivilegeEntity save(PrivilegeEntity privilege) {
        Set<Long> oldRoleIds;
        if (privilege.getId() == null) {
            privilegeRepository.findByName(privilege.getName()).ifPresent((p) -> {
                throw new PrivilegeExistsException();
            });
            oldRoleIds = Collections.emptySet();
        } else {
            oldRoleIds = this.findOne(privilege.getId()).getRoles()
                    .stream().map(RoleEntity::getId).collect(Collectors.toSet());
        }
        Set<Long> newRoleIds = privilege.getRoles()
                .stream().map(RoleEntity::getId).collect(Collectors.toSet());

        PrivilegeEntity privilegeSave = privilegeRepository.save(privilege);

        roleService.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilegeSave);

        return privilegeSave;
    }

    @Override
    public void delete(Long id) {
        PrivilegeEntity exercise = this.findOne(id);
        privilegeRepository.delete(exercise);
    }

    @Override
    public boolean exists(Long id) {
        return privilegeRepository.existsById(id);
    }
}
