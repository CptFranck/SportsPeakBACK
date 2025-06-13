package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.config.security.TokenHashConfig;
import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TokenType;
import com.CptFranck.SportsPeak.domain.exception.token.TokenNotFoundException;
import com.CptFranck.SportsPeak.repository.TokenRepository;
import com.CptFranck.SportsPeak.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    private final TokenHashConfig.Sha256Hasher sha256Hasher;

    public TokenServiceImpl(TokenRepository tokenRepository, PasswordEncoder passwordEncoder, TokenHashConfig.Sha256Hasher sha256Hasher) {
        this.sha256Hasher = sha256Hasher;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String hashToken(String token) {
        return sha256Hasher.hash(token);
    }

    @Override
    public void revokeUserAccessTokens(UserEntity user) {
        List<TokenEntity> tokens = tokenRepository.findAllValidTokenByUser(user.getId())
                .stream()
                .filter(t -> t.getTokenType() == TokenType.ACCESS)
                .toList();
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void revokeAllUserTokens(UserEntity user) {
        List<TokenEntity> tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public TokenEntity save(TokenEntity token) {
        return tokenRepository.save(token);
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenRepository.findByToken(hashToken(token))
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    @Override
    public void revokeToken(String token) {
        TokenEntity tokenEntity = tokenRepository.findByToken(hashToken(token)).orElseThrow(TokenNotFoundException::new);
        tokenEntity.setExpired(true);
        tokenEntity.setRevoked(true);
        tokenRepository.save(tokenEntity);
    }
}

