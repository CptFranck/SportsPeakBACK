package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PrivilegeService {

    List<PrivilegeEntity> findAll();

    Optional<PrivilegeEntity> findOne(Long id);

    Set<PrivilegeEntity> findMany(Set<Long> ids);

    boolean exists(Long id);

    PrivilegeEntity save(PrivilegeEntity privilegeEntity);

    void delete(Long id);
}