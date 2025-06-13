package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.config.security.TokenHashConfig;
import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.token.TokenNotFoundException;
import com.CptFranck.SportsPeak.repository.TokenRepository;
import com.CptFranck.SportsPeak.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    private final TokenHashConfig.Sha256Hasher sha256Hasher;

    public TokenServiceImpl(TokenRepository tokenRepository, TokenHashConfig.Sha256Hasher sha256Hasher) {
        this.sha256Hasher = sha256Hasher;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String hashToken(String token) {
        return sha256Hasher.hash(token);
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
        Optional<TokenEntity> test = tokenRepository.findByToken(hashToken(token));
        return test.map(t -> !t.isExpired() && !t.isRevoked())
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

