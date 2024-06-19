package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {

    List<RoleEntity> findAll();

    Optional<RoleEntity> findOne(Long id);

    Optional<RoleEntity> findByName(String name);

    Set<RoleEntity> findMany(Set<Long> ids);

    void updatePrivilegeRelation(Set<Long> newIds, Set<Long> oldIds, PrivilegeEntity privilegeEntity);

    boolean exists(Long id);

    RoleEntity save(RoleEntity roleEntity);

    void delete(Long id);
}