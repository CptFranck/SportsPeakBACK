package com.CptFranck.SportsPeak.repository;

import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query("SELECT t FROM TokenEntity t" +
            " INNER JOIN UserEntity u on u.id = t.user.id" +
            " WHERE u.id = :userId AND t.expired = false AND t.revoked = false")
    List<TokenEntity> findAllValidTokenByUser(Long userId);

    Optional<TokenEntity> findByToken(String token);
}
