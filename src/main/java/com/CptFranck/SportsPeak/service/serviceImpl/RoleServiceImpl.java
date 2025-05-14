package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
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
    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Set<RoleEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(roleRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
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
        throw new PrivilegeNotFoundException(role.getId());
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

    @Override
    public void delete(Long id) {
        RoleEntity role = this.findOne(id);
        roleRepository.delete(role);
    }
}
