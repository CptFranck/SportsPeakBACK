package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    List<UserEntity> findAll();

    Optional<UserEntity> findOne(Long id);

    Set<UserEntity> findMany(Set<Long> ids);

    void updateRoleRelation(Set<Long> newIds, Set<Long> oldIds, RoleEntity roleEntity);

    boolean exists(Long id);

    void delete(Long id);

    UserEntity changeIdentity(Long id, String firstName, String lastName);

    UserEntity changeRoles(Long id, Collection<RoleEntity> roles);

    UserEntity changeEmail(Long id, String password, String newEmail);

    UserEntity changeUsername(Long id, String newUsername);

    UserEntity changePassword(Long id, String oldPassword, String newPassword);
}