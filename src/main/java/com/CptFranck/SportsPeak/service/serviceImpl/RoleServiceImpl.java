package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleStillUsedByPrivilegeException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleStillUsedByUserException;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    public List<RoleEntity> findAll() {
        return StreamSupport.stream(roleRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public RoleEntity findOne(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id.toString()));
    }

    @Override
    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException(name));
    }

    @Override
    public Set<RoleEntity> findMany(Set<Long> ids) {
        return new HashSet<>(roleRepository.findAllById(ids));
    }

    @Override
    public RoleEntity save(RoleEntity role) {
        roleRepository.findByName(role.getName()).ifPresent((p) -> {
            if (!Objects.equals(p.getId(), role.getId())) throw new RoleExistsException();
        });
        if (role.getId() == null)
            return roleRepository.save(role);
        if (exists(role.getId()))
            return roleRepository.save(role);
        throw new RoleNotFoundException(role.getId().toString());
    }

    @Override
    public void delete(Long id) {
        RoleEntity role = this.findOne(id);
        if (role.getUsers().isEmpty())
            if (role.getPrivileges().isEmpty())
                roleRepository.delete(role);
            else
                throw new RoleStillUsedByPrivilegeException(id, (long) role.getPrivileges().size());
        else
            throw new RoleStillUsedByUserException(id, (long) role.getUsers().size());
    }

    @Override
    public void updatePrivilegeRelation(Set<Long> newIds, Set<Long> oldIds, PrivilegeEntity privilegeEntity) {
        findMany(oldIds).forEach(p -> {
            p.getPrivileges().removeIf(et -> Objects.equals(et.getId(), privilegeEntity.getId()));
            save(p);
        });
        findMany(newIds).forEach(p -> {
            p.getPrivileges().add(privilegeEntity);
            save(p);
        });
    }

    @Override
    public boolean exists(Long id) {
        return roleRepository.existsById(id);
    }
}
