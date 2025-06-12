package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {
    Optional<PrivilegeEntity> findByName(String name);
}
