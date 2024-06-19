package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrivilegeRepository extends CrudRepository<PrivilegeEntity, Long> {
    Optional<PrivilegeEntity> findByName(String name);
}
