package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    List<UserEntity> findAll();

    Optional<UserEntity> findOne(Long id);

    Set<UserEntity> findMany(Set<Long> ids);

    boolean exists(Long id);

    MuscleEntity save(UserEntity userEntity);

    void delete(Long id);
}