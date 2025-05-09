package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;

import java.util.List;
import java.util.Set;

public interface PrivilegeService {

    List<PrivilegeEntity> findAll();

    PrivilegeEntity findOne(Long id);

    Set<PrivilegeEntity> findMany(Set<Long> ids);

    PrivilegeEntity save(PrivilegeEntity privilegeEntity);

    void delete(Long id);

    boolean exists(Long id);
}