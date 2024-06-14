package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String login);

    Optional<UserEntity> findByUsername(String login);
}
