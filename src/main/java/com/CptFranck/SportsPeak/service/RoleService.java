package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<RoleEntity> findAll();

    RoleEntity findOne(Long id);

    RoleEntity findByName(String name);

    Set<RoleEntity> findMany(Set<Long> ids);

    RoleEntity save(RoleEntity roleEntity);

    void updatePrivilegeRelation(Set<Long> newIds, Set<Long> oldIds, PrivilegeEntity privilegeEntity);

    void delete(Long id);

    boolean exists(Long id);
}