package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;

public interface TokenService {

    TokenEntity save(TokenEntity token);

    boolean isValidToken(String token);

    void revokeAllUserTokens(UserEntity user);
}