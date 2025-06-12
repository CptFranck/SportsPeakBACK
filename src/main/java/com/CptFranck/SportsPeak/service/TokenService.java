package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

public interface TokenService {

    String hashToken(String token);

    TokenEntity save(TokenEntity token);

    boolean isValidToken(String token);

    void revokeToken(String token);

    void revokeAllUserTokens(UserEntity user);
}