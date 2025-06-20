package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeExistsException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.privilege.PrivilegeStillUsedByRoleException;
import com.CptFranck.SportsPeak.repository.PrivilegeRepository;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public List<PrivilegeEntity> findAll() {
        return new ArrayList<>(privilegeRepository.findAll());
    }

    @Override
    public PrivilegeEntity findOne(Long id) {
        return privilegeRepository.findById(id).orElseThrow(() -> new PrivilegeNotFoundException(id));
    }

    @Override
    public Set<PrivilegeEntity> findMany(Set<Long> ids) {
        return new HashSet<>(privilegeRepository.findAllById(ids));
    }

    @Override
    public PrivilegeEntity save(PrivilegeEntity privilege) {
        privilegeRepository.findByName(privilege.getName()).ifPresent((p) -> {
            if (!Objects.equals(p.getId(), privilege.getId())) throw new PrivilegeExistsException();
        });
        if (privilege.getId() == null)
            return privilegeRepository.save(privilege);
        if (exists(privilege.getId()))
            return privilegeRepository.save(privilege);
        throw new PrivilegeNotFoundException(privilege.getId());
    }

    @Override
    public void delete(Long id) {
        PrivilegeEntity privilege = this.findOne(id);
        if (privilege.getRoles().isEmpty())
            privilegeRepository.delete(privilege);
        else
            throw new PrivilegeStillUsedByRoleException(id, (long) privilege.getRoles().size());
    }

    @Override
    public boolean exists(Long id) {
        return privilegeRepository.existsById(id);
    }
}
