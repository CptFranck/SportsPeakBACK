package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity findOne(Long id);

    Set<UserEntity> findMany(Set<Long> ids);

    Set<UserEntity> findUserBySubscribedProgExercises(ProgExerciseEntity progExercise);

    UserEntity findByEmail(String email);

    UserEntity save(UserEntity user);

    void delete(Long id);

    UserEntity changeIdentity(Long id, String firstName, String lastName);

    UserEntity changeRoles(Long id, Set<RoleEntity> roles);

    UserEntity changeUsername(Long id, String newUsername);

    void updateRoleRelation(Set<Long> newIds, Set<Long> oldIds, RoleEntity roleEntity);

    boolean exists(Long id);
}