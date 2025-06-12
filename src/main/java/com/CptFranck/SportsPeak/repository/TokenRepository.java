package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
}
